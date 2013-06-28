package com.qsoft.longdt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class BankAccountTest extends TestCase {

	private BankAccount ba;
	private Transaction trans;
	private BankAccountDAO baDAO;
	private TransactionDAO tDAO;
	private String accountNumber;
	private Calendar mockCal;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		MockitoAnnotations.initMocks(this);
		baDAO = mock(BankAccountDAO.class);
		tDAO = mock(TransactionDAO.class);
		ba = new BankAccount();
		trans = new Transaction();
		ba.setBaDAO(baDAO);
		ba.setTDAO(tDAO);
		trans.setTransactionDao(tDAO);
		mockCal = mock(Calendar.class);
		accountNumber = "0012973939";
	}

	@Test
	public void testOpenAccountWithZeroBalance() {
		ba.openAccount(accountNumber);
		ArgumentCaptor<BankAccountDTO> arg = ArgumentCaptor
				.forClass(BankAccountDTO.class);
		verify(baDAO).doCreate(arg.capture());
		assertEquals(0, arg.getValue().getBalance(), 0.01);
	}

	@Test
	public void testGetAccount() {
		BankAccountDTO accountOpened = ba.openAccount(accountNumber);
		when(baDAO.doRead(accountNumber)).thenReturn(accountOpened);

		BankAccountDTO accountRead = ba.getAccount(accountNumber);
		assertEquals(accountNumber, accountRead.getAccountNumber());

		verify(baDAO, times(1)).doCreate(accountOpened);
		assertEquals(accountOpened, accountRead);
	}

	@Test
	public void testDepositSaveToDB() {
		ArgumentCaptor<BankAccountDTO> argOpen = ArgumentCaptor
				.forClass(BankAccountDTO.class);
		ArgumentCaptor<BankAccountDTO> argDep = ArgumentCaptor
				.forClass(BankAccountDTO.class);
		BankAccountDTO accountOpened = ba.openAccount(accountNumber);

		verify(baDAO, times(1)).doCreate(argOpen.capture());
		assertEquals(accountNumber, argOpen.getValue().getAccountNumber());
		assertEquals(0, argOpen.getValue().getBalance(), 0.01);

		when(baDAO.doRead(accountNumber)).thenReturn(accountOpened);

		ba.deposit(accountOpened.getAccountNumber(), 100l, "Deposit 100");

		verify(baDAO, times(1)).doUpdate(argDep.capture());
		assertEquals(accountNumber, argDep.getValue().getAccountNumber());
		assertEquals(100, argDep.getValue().getBalance(), 0.01);

	}

	@Test
	public void testDepositSaveToDBWithTimeStamp() {
		trans.createTransaction(accountNumber, 100, "de 100k", 123123123l);

		ArgumentCaptor<TransactionDTO> argumentTransaction = ArgumentCaptor
				.forClass(TransactionDTO.class);
		verify(tDAO, times(1)).doUpdate(argumentTransaction.capture());
		assertEquals(accountNumber, argumentTransaction.getValue()
				.getAccountNumber());
		assertEquals(100, argumentTransaction.getValue().getAmount(), 0.01);
		assertEquals("de 100k", argumentTransaction.getValue().getDescription());
		assertTrue(argumentTransaction.getValue().getTimestamp() != 0);
	}
}

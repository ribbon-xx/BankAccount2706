package com.qsoft.longdt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class BankAccountTest extends TestCase {

	private BankAccount ba;
	private BankAccountDAO baDAO;
	private String accountNumber;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		MockitoAnnotations.initMocks(this);
		baDAO = mock(BankAccountDAO.class);
		ba = new BankAccount();
		ba.setBaDAO(baDAO);
		accountNumber = "0012973939";
	}

	@Test
	public void testOpenAccountWithZeroBalance() {
		BankAccountDTO accountOpened = ba.openAccount(accountNumber);
		assertEquals(0, accountOpened.getBalance(), 0.01);
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
}

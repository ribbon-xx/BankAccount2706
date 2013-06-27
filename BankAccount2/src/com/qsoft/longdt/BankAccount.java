package com.qsoft.longdt;

public class BankAccount {

	private BankAccountDAO baDAO;

	public void setBaDAO(BankAccountDAO baDAO) {
		this.baDAO = baDAO;
	}

	public BankAccountDTO openAccount(String accountNumber) {
		BankAccountDTO baDTO = new BankAccountDTO();
		baDTO.setAccountNumber(accountNumber);
		baDTO.setBalance(0l);
		baDAO.doCreate(baDTO);
		return baDTO;
	}

	public BankAccountDTO getAccount(String accountNumber) {
		return baDAO.doRead(accountNumber);
	}

}

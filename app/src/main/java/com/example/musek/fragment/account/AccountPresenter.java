package com.example.musek.fragment.account;

public class AccountPresenter {
    private AccountInterface anInterface;

    public AccountPresenter(AccountInterface anInterface) {
        this.anInterface = anInterface;
    }
    public void setAdapter (){
        anInterface.setAdapter();
    }
    public void checkLogin (String phoneNumber, int gravity){
        if (phoneNumber == null){
            anInterface.openDialogLogin(gravity);
        }else {
            anInterface.openDialogLogout(gravity);
        }
    }
}

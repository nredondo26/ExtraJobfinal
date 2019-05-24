package nredondo26.com.extrajob;

import com.google.firebase.auth.FirebaseUser;

public class FirebaseAutenticacion {

    public static boolean Auth(){
        com.google.firebase.auth.FirebaseAuth firebaseAuth= com.google.firebase.auth.FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            return true;
        }else{
            return false;
        }
    }

    public static FirebaseUser Auth_user() {
        com.google.firebase.auth.FirebaseAuth firebaseAuth= com.google.firebase.auth.FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser();
    }


}

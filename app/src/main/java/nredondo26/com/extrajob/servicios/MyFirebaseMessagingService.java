package nredondo26.com.extrajob.servicios;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import static android.support.constraint.Constraints.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    FirebaseFirestore db;
    SharedPreferences usertodo;

    @Override
    public void onNewToken(String token){

        usertodo = this.getSharedPreferences("detallesusuario", MODE_PRIVATE);
        int tipo = usertodo.getInt("tipousuario",0);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;

        DocumentReference washingtonRef;

        if(tipo==1){
            washingtonRef = db.collection("usuarios").document(user.getUid());
        }
        else{
            washingtonRef = db.collection("empresa").document(user.getUid());
        }

        washingtonRef.update("Token", token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Token Actualizado!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error al actualizar el token", e);
                    }
                });
    }

}
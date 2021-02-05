package com.sh.pytalapp.database;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.pytalapp.model.User;
import com.sh.pytalapp.utils.Const;
import com.sh.pytalapp.utils.StringFormatUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FirebaseResource {

    public static void createRandomUser(int quantity, String role) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Const.FIREBASE_REF.USER);

        List<String> listCodeOTP = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            listCodeOTP.add(StringFormatUtils.getAlphaNumericString(6));
        }
        Set<String> setData = new HashSet<>(listCodeOTP);
        listCodeOTP.clear();
        listCodeOTP.addAll(setData);

        for (int i = 0; i < listCodeOTP.size(); i++) {
            User user = User.builder()
                    .role(role)
                    .lastLogin("")
                    .serialDevice("")
                    .codeOTP(listCodeOTP.get(i))
                    .createdDate(StringFormatUtils.getCurrentDateStr())
                    .build();

            mDatabase.child(listCodeOTP.get(i)).setValue(user);
        }
    }

    public static List<User> getAllUsers() {
        final List<User> listUsers = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Const.FIREBASE_REF.USER);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnap : snapshot.getChildren()) {
                    User user = dataSnap.getValue(User.class);
                    if (user != null) {
                        listUsers.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return listUsers;
    }

    public static void deleteUser(User user) {
        FirebaseDatabase.getInstance().getReference()
                .child(Const.FIREBASE_REF.USER)
                .child(user.getCodeOTP())
                .removeValue();
    }
}

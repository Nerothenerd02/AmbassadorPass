// Generated by view binder compiler. Do not edit!
package com.example.ambassadorpass.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.ambassadorpass.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogAssignAmbassadorBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final EditText ambassadorEmail;

  @NonNull
  public final EditText ambassadorName;

  @NonNull
  public final Button sendButton;

  private DialogAssignAmbassadorBinding(@NonNull CardView rootView,
      @NonNull EditText ambassadorEmail, @NonNull EditText ambassadorName,
      @NonNull Button sendButton) {
    this.rootView = rootView;
    this.ambassadorEmail = ambassadorEmail;
    this.ambassadorName = ambassadorName;
    this.sendButton = sendButton;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogAssignAmbassadorBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogAssignAmbassadorBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_assign_ambassador, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogAssignAmbassadorBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ambassadorEmail;
      EditText ambassadorEmail = ViewBindings.findChildViewById(rootView, id);
      if (ambassadorEmail == null) {
        break missingId;
      }

      id = R.id.ambassadorName;
      EditText ambassadorName = ViewBindings.findChildViewById(rootView, id);
      if (ambassadorName == null) {
        break missingId;
      }

      id = R.id.sendButton;
      Button sendButton = ViewBindings.findChildViewById(rootView, id);
      if (sendButton == null) {
        break missingId;
      }

      return new DialogAssignAmbassadorBinding((CardView) rootView, ambassadorEmail, ambassadorName,
          sendButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
// Generated by view binder compiler. Do not edit!
package com.example.ambassadorpass.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.ambassadorpass.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityPageOneBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageButton backButton;

  @NonNull
  public final ImageView logoImageView;

  @NonNull
  public final TextView partyname;

  @NonNull
  public final Button proceedButton;

  @NonNull
  public final TextView typewriter;

  @NonNull
  public final FrameLayout typewriterContainer;

  private ActivityPageOneBinding(@NonNull RelativeLayout rootView, @NonNull ImageButton backButton,
      @NonNull ImageView logoImageView, @NonNull TextView partyname, @NonNull Button proceedButton,
      @NonNull TextView typewriter, @NonNull FrameLayout typewriterContainer) {
    this.rootView = rootView;
    this.backButton = backButton;
    this.logoImageView = logoImageView;
    this.partyname = partyname;
    this.proceedButton = proceedButton;
    this.typewriter = typewriter;
    this.typewriterContainer = typewriterContainer;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityPageOneBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityPageOneBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_page_one, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityPageOneBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backButton;
      ImageButton backButton = ViewBindings.findChildViewById(rootView, id);
      if (backButton == null) {
        break missingId;
      }

      id = R.id.logoImageView;
      ImageView logoImageView = ViewBindings.findChildViewById(rootView, id);
      if (logoImageView == null) {
        break missingId;
      }

      id = R.id.partyname;
      TextView partyname = ViewBindings.findChildViewById(rootView, id);
      if (partyname == null) {
        break missingId;
      }

      id = R.id.proceedButton;
      Button proceedButton = ViewBindings.findChildViewById(rootView, id);
      if (proceedButton == null) {
        break missingId;
      }

      id = R.id.typewriter;
      TextView typewriter = ViewBindings.findChildViewById(rootView, id);
      if (typewriter == null) {
        break missingId;
      }

      id = R.id.typewriterContainer;
      FrameLayout typewriterContainer = ViewBindings.findChildViewById(rootView, id);
      if (typewriterContainer == null) {
        break missingId;
      }

      return new ActivityPageOneBinding((RelativeLayout) rootView, backButton, logoImageView,
          partyname, proceedButton, typewriter, typewriterContainer);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

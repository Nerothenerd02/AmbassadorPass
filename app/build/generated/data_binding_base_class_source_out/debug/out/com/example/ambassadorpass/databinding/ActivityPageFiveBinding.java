// Generated by view binder compiler. Do not edit!
package com.example.ambassadorpass.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.ambassadorpass.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityPageFiveBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageButton backButton;

  @NonNull
  public final Button confirmButton;

  @NonNull
  public final LinearLayout detailsContainer;

  @NonNull
  public final ImageView logoImageView;

  @NonNull
  public final TextView partyDetailsTextView;

  @NonNull
  public final TextView titleTextView;

  @NonNull
  public final TextView userDetailsTextView;

  private ActivityPageFiveBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageButton backButton, @NonNull Button confirmButton,
      @NonNull LinearLayout detailsContainer, @NonNull ImageView logoImageView,
      @NonNull TextView partyDetailsTextView, @NonNull TextView titleTextView,
      @NonNull TextView userDetailsTextView) {
    this.rootView = rootView;
    this.backButton = backButton;
    this.confirmButton = confirmButton;
    this.detailsContainer = detailsContainer;
    this.logoImageView = logoImageView;
    this.partyDetailsTextView = partyDetailsTextView;
    this.titleTextView = titleTextView;
    this.userDetailsTextView = userDetailsTextView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityPageFiveBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityPageFiveBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_page_five, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityPageFiveBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backButton;
      ImageButton backButton = ViewBindings.findChildViewById(rootView, id);
      if (backButton == null) {
        break missingId;
      }

      id = R.id.confirmButton;
      Button confirmButton = ViewBindings.findChildViewById(rootView, id);
      if (confirmButton == null) {
        break missingId;
      }

      id = R.id.detailsContainer;
      LinearLayout detailsContainer = ViewBindings.findChildViewById(rootView, id);
      if (detailsContainer == null) {
        break missingId;
      }

      id = R.id.logoImageView;
      ImageView logoImageView = ViewBindings.findChildViewById(rootView, id);
      if (logoImageView == null) {
        break missingId;
      }

      id = R.id.partyDetailsTextView;
      TextView partyDetailsTextView = ViewBindings.findChildViewById(rootView, id);
      if (partyDetailsTextView == null) {
        break missingId;
      }

      id = R.id.titleTextView;
      TextView titleTextView = ViewBindings.findChildViewById(rootView, id);
      if (titleTextView == null) {
        break missingId;
      }

      id = R.id.userDetailsTextView;
      TextView userDetailsTextView = ViewBindings.findChildViewById(rootView, id);
      if (userDetailsTextView == null) {
        break missingId;
      }

      return new ActivityPageFiveBinding((ConstraintLayout) rootView, backButton, confirmButton,
          detailsContainer, logoImageView, partyDetailsTextView, titleTextView,
          userDetailsTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

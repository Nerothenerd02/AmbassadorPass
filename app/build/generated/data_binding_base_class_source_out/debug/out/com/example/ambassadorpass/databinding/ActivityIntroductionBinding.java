// Generated by view binder compiler. Do not edit!
package com.example.ambassadorpass.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public final class ActivityIntroductionBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageButton backButton;

  @NonNull
  public final TextView descriptionText;

  @NonNull
  public final Button getStartedButton;

  @NonNull
  public final TextView introductionText;

  @NonNull
  public final ImageView logoImageView;

  private ActivityIntroductionBinding(@NonNull RelativeLayout rootView,
      @NonNull ImageButton backButton, @NonNull TextView descriptionText,
      @NonNull Button getStartedButton, @NonNull TextView introductionText,
      @NonNull ImageView logoImageView) {
    this.rootView = rootView;
    this.backButton = backButton;
    this.descriptionText = descriptionText;
    this.getStartedButton = getStartedButton;
    this.introductionText = introductionText;
    this.logoImageView = logoImageView;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityIntroductionBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityIntroductionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_introduction, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityIntroductionBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backButton;
      ImageButton backButton = ViewBindings.findChildViewById(rootView, id);
      if (backButton == null) {
        break missingId;
      }

      id = R.id.descriptionText;
      TextView descriptionText = ViewBindings.findChildViewById(rootView, id);
      if (descriptionText == null) {
        break missingId;
      }

      id = R.id.getStartedButton;
      Button getStartedButton = ViewBindings.findChildViewById(rootView, id);
      if (getStartedButton == null) {
        break missingId;
      }

      id = R.id.introductionText;
      TextView introductionText = ViewBindings.findChildViewById(rootView, id);
      if (introductionText == null) {
        break missingId;
      }

      id = R.id.logoImageView;
      ImageView logoImageView = ViewBindings.findChildViewById(rootView, id);
      if (logoImageView == null) {
        break missingId;
      }

      return new ActivityIntroductionBinding((RelativeLayout) rootView, backButton, descriptionText,
          getStartedButton, introductionText, logoImageView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

// Generated by view binder compiler. Do not edit!
package com.example.ambassadorpass.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.ambassadorpass.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FooterAnalysisBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView ambassadorDetails;

  @NonNull
  public final TextView totalAttendees;

  @NonNull
  public final TextView totalEarnings;

  private FooterAnalysisBinding(@NonNull LinearLayout rootView, @NonNull TextView ambassadorDetails,
      @NonNull TextView totalAttendees, @NonNull TextView totalEarnings) {
    this.rootView = rootView;
    this.ambassadorDetails = ambassadorDetails;
    this.totalAttendees = totalAttendees;
    this.totalEarnings = totalEarnings;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FooterAnalysisBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FooterAnalysisBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.footer_analysis, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FooterAnalysisBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ambassadorDetails;
      TextView ambassadorDetails = ViewBindings.findChildViewById(rootView, id);
      if (ambassadorDetails == null) {
        break missingId;
      }

      id = R.id.totalAttendees;
      TextView totalAttendees = ViewBindings.findChildViewById(rootView, id);
      if (totalAttendees == null) {
        break missingId;
      }

      id = R.id.totalEarnings;
      TextView totalEarnings = ViewBindings.findChildViewById(rootView, id);
      if (totalEarnings == null) {
        break missingId;
      }

      return new FooterAnalysisBinding((LinearLayout) rootView, ambassadorDetails, totalAttendees,
          totalEarnings);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
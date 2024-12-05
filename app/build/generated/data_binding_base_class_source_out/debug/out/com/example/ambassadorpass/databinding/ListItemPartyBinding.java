// Generated by view binder compiler. Do not edit!
package com.example.ambassadorpass.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.ambassadorpass.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ListItemPartyBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final LinearLayout extraInfoContainer;

  @NonNull
  public final TextView extraInfoText;

  @NonNull
  public final FloatingActionButton partyActionButton;

  @NonNull
  public final CardView partyCard;

  @NonNull
  public final TextView partyDate;

  @NonNull
  public final TextView partyId;

  @NonNull
  public final ShapeableImageView partyImage;

  @NonNull
  public final TextView partyName;

  private ListItemPartyBinding(@NonNull CardView rootView, @NonNull LinearLayout extraInfoContainer,
      @NonNull TextView extraInfoText, @NonNull FloatingActionButton partyActionButton,
      @NonNull CardView partyCard, @NonNull TextView partyDate, @NonNull TextView partyId,
      @NonNull ShapeableImageView partyImage, @NonNull TextView partyName) {
    this.rootView = rootView;
    this.extraInfoContainer = extraInfoContainer;
    this.extraInfoText = extraInfoText;
    this.partyActionButton = partyActionButton;
    this.partyCard = partyCard;
    this.partyDate = partyDate;
    this.partyId = partyId;
    this.partyImage = partyImage;
    this.partyName = partyName;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ListItemPartyBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ListItemPartyBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.list_item_party, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ListItemPartyBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.extraInfoContainer;
      LinearLayout extraInfoContainer = ViewBindings.findChildViewById(rootView, id);
      if (extraInfoContainer == null) {
        break missingId;
      }

      id = R.id.extraInfoText;
      TextView extraInfoText = ViewBindings.findChildViewById(rootView, id);
      if (extraInfoText == null) {
        break missingId;
      }

      id = R.id.partyActionButton;
      FloatingActionButton partyActionButton = ViewBindings.findChildViewById(rootView, id);
      if (partyActionButton == null) {
        break missingId;
      }

      CardView partyCard = (CardView) rootView;

      id = R.id.partyDate;
      TextView partyDate = ViewBindings.findChildViewById(rootView, id);
      if (partyDate == null) {
        break missingId;
      }

      id = R.id.partyId;
      TextView partyId = ViewBindings.findChildViewById(rootView, id);
      if (partyId == null) {
        break missingId;
      }

      id = R.id.partyImage;
      ShapeableImageView partyImage = ViewBindings.findChildViewById(rootView, id);
      if (partyImage == null) {
        break missingId;
      }

      id = R.id.partyName;
      TextView partyName = ViewBindings.findChildViewById(rootView, id);
      if (partyName == null) {
        break missingId;
      }

      return new ListItemPartyBinding((CardView) rootView, extraInfoContainer, extraInfoText,
          partyActionButton, partyCard, partyDate, partyId, partyImage, partyName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
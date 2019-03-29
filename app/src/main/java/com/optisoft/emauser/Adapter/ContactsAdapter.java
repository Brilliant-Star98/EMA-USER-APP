package com.optisoft.emauser.Adapter;

/**
 * Created by OptiSoft_A on 7/3/2018.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.VisitorModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.optisoft.emauser.HelperClasses.ApiConstant.IMAGE_URL;

/**
 * Created by ravi on 16/11/17.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<VisitorModel> contactList;
    private List<VisitorModel> contactListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone, mPerson, mInDate, mStatus;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.title);
            //mUserName = view.findViewById(R.id.user_name);
            phone = view.findViewById(R.id.phone);
            thumbnail = view.findViewById(R.id.image);
            //mUserName = (TextView) view.findViewById(R.id.user_name);
            mPerson = (TextView) view.findViewById(R.id.user_count);
            mInDate = (TextView) view.findViewById(R.id.in_date);
            mStatus = (TextView) view.findViewById(R.id.status);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });

            mStatus.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    alertDialogBuilder.setTitle("Cancel Reservation?");
                    alertDialogBuilder
                            .setMessage("Click YES to cancel this reservation")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    VisitorModel selectedModel = contactListFiltered.get(getAdapterPosition());
                                    listener.onCancelSelected(selectedModel);

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }
    }


    public ContactsAdapter(Context context, List<VisitorModel> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_visitor, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final VisitorModel contact = contactListFiltered.get(position);
        holder.name.setText(contact.getVisitor_name());
        holder.phone.setText(contact.getMobile());
        holder.mInDate.setText(convertDate(contact.getDate_in()));
        holder.mPerson.setText(contact.getNum_person());
        //holder.mUserName.setText(contact.getVisit_to());

        switch (contact.getStatus()){

            case "0":
                holder.mStatus.setBackgroundColor(Color.parseColor("#16b728")); //f1a92d
                holder.mStatus.setText("Scheduled");
                break;

            case "1":
                holder.mStatus.setBackgroundColor(Color.parseColor("#16b728"));
                holder.mStatus.setText("Accepted");
                break;

            case "2":
                holder.mStatus.setBackgroundColor(Color.parseColor("#ff0000"));
                holder.mStatus.setText("Rejected");
                break;

            case "3":
                holder.mStatus.setBackgroundColor(Color.parseColor("#5a5858"));
                holder.mStatus.setText("Closed");
                break;

            case "4":
                holder.mStatus.setBackgroundColor(Color.parseColor("#000000"));
                holder.mStatus.setText("Deleted");
                break;

            default:
        }

        Picasso.with(context)
                .load(IMAGE_URL+contact.getImage())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<VisitorModel> filteredList = new ArrayList<>();
                    for (VisitorModel row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getVisitor_name().toLowerCase().contains(charString.toLowerCase()) || row.getMobile().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<VisitorModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(VisitorModel contact);
        void onCancelSelected(VisitorModel contact);
    }

    public String convertDate(String ds1){
        String ds2 = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d, yyyy");
        try {
            ds2 = sdf2.format(sdf1.parse(ds1));
        } catch (ParseException e) {
            e.printStackTrace();
            return ds1;
        }

        return ds2;
    }
}
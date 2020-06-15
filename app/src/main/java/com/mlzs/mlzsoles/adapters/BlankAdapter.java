package com.mlzs.mlzsoles.adapters;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.fragments.ExamBlankFragment;


public class BlankAdapter extends RecyclerView.Adapter<BlankAdapter.ViewHolder> {

    int listSize;
    Context context;
    ExamBlankFragment fragment;


    public BlankAdapter(Context context, int listSize, ExamBlankFragment fragment) {

        this.context = context;
        this.listSize = listSize;
        this.fragment = fragment;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_blank, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final int count = position + 1;

        holder.edBlanAns.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    fragment.addBlankValues(holder.edBlanAns.getText().toString(), position);
                    hideSoftKeyboard(holder.edBlanAns);
                }
            }
        });

        holder.tvIndex.setText(" " + count + " ");

    }

    @Override
    public int getItemCount() {
        return listSize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText edBlanAns;
        TextView tvIndex;

        public ViewHolder(View itemView) {
            super(itemView);

            edBlanAns = (EditText) itemView.findViewById(R.id.ed_row_blank_ans);
            tvIndex = (TextView) itemView.findViewById(R.id.tv_blanck_index);
        }
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

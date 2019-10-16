package org.totschnig.myexpenses.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import org.totschnig.myexpenses.R;
import org.totschnig.myexpenses.activity.MyExpenses;
import org.totschnig.myexpenses.provider.filter.DateCriteria;
import org.totschnig.myexpenses.provider.filter.WhereFilter;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class DateFilterDialog extends CommitSafeDialogFragment implements OnClickListener {
  private DatePicker mDate1;
  private DatePicker mDate2;
  private Spinner mOperatorSpinner;
  public static DateFilterDialog newInstance() {
    return new DateFilterDialog();
  }
  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    MyExpenses ctx  = (MyExpenses) getActivity();
    LayoutInflater li = LayoutInflater.from(ctx);
    //noinspection InflateParams
    View view = li.inflate(R.layout.filter_date, null);
    mOperatorSpinner = view.findViewById(R.id.Operator);
    final View date2And = view.findViewById(R.id.Date2And);
    mOperatorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> parent, View view,
          int position, long id) {
        String selectedOp = getResources().getStringArray(R.array.comparison_operator_date_values)[position];
        final int date2Visible = selectedOp.equals("BTW") ? View.VISIBLE : View.GONE;
        date2And.setVisibility(date2Visible);
        mDate2.setVisibility(date2Visible);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
      }
    });
    ((ArrayAdapter) mOperatorSpinner.getAdapter())
        .setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

    mDate1 = view.findViewById(R.id.date1);
    mDate2 = view.findViewById(R.id.date2);


    return new AlertDialog.Builder(ctx)
      .setTitle(R.string.search_date)
      .setView(view)
      .setPositiveButton(android.R.string.ok,this)
      .setNegativeButton(android.R.string.cancel,null)
      .create();
  }
  @Override
  public void onClick(DialogInterface dialog, int which) {
    MyExpenses ctx = (MyExpenses) getActivity();
    long date1, date2;
    DateCriteria c;
    if (ctx==null) {
      return;
    }
    Calendar cal = Calendar.getInstance();
    String selectedOp = getResources().getStringArray(R.array.comparison_operator_date_values)
        [mOperatorSpinner.getSelectedItemPosition()];
    if (selectedOp.equals("GTE")) {
      //advance to end of day
      cal.set(mDate1.getYear(),mDate1.getMonth(),mDate1.getDayOfMonth(),23,59,59);
      date1 = cal.getTimeInMillis()/1000+1;
      } else {
      cal.set(mDate1.getYear(), mDate1.getMonth(), mDate1.getDayOfMonth(), 0, 0, 0);
      date1 = cal.getTimeInMillis() / 1000;
    }
    if (selectedOp.equals("BTW")) {
      //advance to end of day
      cal.set(mDate2.getYear(),mDate2.getMonth(),mDate2.getDayOfMonth(),23,59,59);
      date2 = cal.getTimeInMillis()/1000+1;
      c=new DateCriteria(date1,date2);
    } else {
      c = new DateCriteria(
          WhereFilter.Operation.valueOf(selectedOp),
          date1);
    }
    ctx.addFilterCriteria(c);
  }
}

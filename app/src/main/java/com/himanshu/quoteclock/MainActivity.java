package com.himanshu.quoteclock;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();
    private TextView clockText;
    private TextView dateText;
    private TextView quoteText;
    private int quoteIndex = -1;

    private final String[] quotes = {
            "Small steps, repeated, become a life.",
            "You do not need a perfect plan to begin.",
            "A calm mind sees options that panic misses.",
            "Consistency is quiet, but it compounds.",
            "The next useful action is usually enough.",
            "Build a day you would be proud to repeat.",
            "Progress can be slow and still be real.",
            "Start before you feel fully ready.",
            "Your future is shaped by ordinary choices.",
            "Focus turns effort into results."
    };

    private final Runnable clockUpdater = new Runnable() {
        @Override public void run() {
            Date now = new Date();
            clockText.setText(new SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(now));
            dateText.setText(new SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault()).format(now));
            handler.postDelayed(this, 1000L);
        }
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(16, 18, 24));
        getWindow().setNavigationBarColor(Color.rgb(16, 18, 24));

        int padding = dp(24);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(padding, dp(30), padding, dp(24));
        root.setBackgroundColor(Color.rgb(16, 18, 24));

        TextView title = text("QUOTE CLOCK", 14, Color.rgb(123, 177, 255));
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setLetterSpacing(0.16f);
        root.addView(title, params(-1, -2));

        addSpace(root, 34);
        dateText = text("", 16, Color.rgb(186, 193, 207));
        root.addView(dateText, params(-1, -2));
        addSpace(root, 8);

        clockText = text("", 42, Color.WHITE);
        clockText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        root.addView(clockText, params(-1, -2));

        View flexibleSpace = new View(this);
        root.addView(flexibleSpace, new LinearLayout.LayoutParams(-1, 0, 1f));

        quoteText = text("", 25, Color.WHITE);
        quoteText.setGravity(Gravity.CENTER);
        quoteText.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        quoteText.setBackgroundColor(Color.rgb(34, 38, 49));
        quoteText.setPadding(dp(22), dp(32), dp(22), dp(32));
        root.addView(quoteText, params(-1, -2));

        addSpace(root, 18);
        Button newQuote = new Button(this);
        newQuote.setText("NEW QUOTE");
        newQuote.setTextColor(Color.WHITE);
        newQuote.setTextSize(14);
        newQuote.setTypeface(Typeface.DEFAULT_BOLD);
        newQuote.setAllCaps(false);
        newQuote.setBackgroundColor(Color.rgb(63, 113, 205));
        newQuote.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showNextQuote(); }
        });
        root.addView(newQuote, params(-1, dp(56)));

        addSpace(root, 16);
        TextView note = text("Works offline • Clock updates every second", 12, Color.rgb(153, 161, 178));
        root.addView(note, params(-1, -2));

        setContentView(root);
        showNextQuote();
        handler.post(clockUpdater);
    }

    private void showNextQuote() {
        int next;
        do { next = random.nextInt(quotes.length); } while (next == quoteIndex && quotes.length > 1);
        quoteIndex = next;
        quoteText.setText("“" + quotes[quoteIndex] + "”\n\n— Quote Clock");
    }

    @Override protected void onDestroy() {
        handler.removeCallbacks(clockUpdater);
        super.onDestroy();
    }

    private TextView text(String value, int sizeSp, int color) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(sizeSp);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    private LinearLayout.LayoutParams params(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }

    private void addSpace(LinearLayout parent, int heightDp) {
        View space = new View(this);
        parent.addView(space, params(-1, dp(heightDp)));
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}

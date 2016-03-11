package com.salve.activities.operations.listeners.handshake;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.salve.R;
import com.salve.activities.MainScreen;
import com.salve.activities.commands.Command;
import com.salve.activities.commands.CommandFactory;
import com.salve.activities.commands.CommandType;
import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.IGestureRecognitionService;

public class TrainButtonOnClickListener implements View.OnClickListener {

    private Button button;
    private Activity activity;

    public TrainButtonOnClickListener(Activity activity, Button button) {
        this.button = button;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {

        IGestureRecognitionService recognitionService = ((MainScreen) activity)
                .getScreenOps()
                .getGestureConnectionService()
                .getRecognitionService();

        Command command;

        if (button.getText() == activity.getResources().getString(R.string.handshake_alertDialog_start)) {
            command = CommandFactory.create(CommandType.StartTraining, recognitionService);
            button.setText(activity.getResources().getString(R.string.handshake_alertDialog_stop));
        } else {
            command = CommandFactory.create(CommandType.StopTraining, recognitionService);
            button.setText(activity.getResources().getString(R.string.handshake_alertDialog_start));
        }

        command.execute();
    }
}

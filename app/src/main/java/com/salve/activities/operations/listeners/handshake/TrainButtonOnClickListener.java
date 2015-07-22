package com.salve.activities.operations.listeners.handshake;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.salve.R;
import com.salve.activities.commands.Command;
import com.salve.activities.commands.CommandFactory;
import com.salve.activities.commands.CommandType;
import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.IGestureRecognitionService;

/**
 * Created by Vlad on 7/22/2015.
 */
public class TrainButtonOnClickListener implements View.OnClickListener {

    private GestureConnectionService gestureConnectionService;
    private Button button;
    private Activity activity;

    public TrainButtonOnClickListener(Activity activity, Button button, GestureConnectionService gestureConnectionService) {
        this.button = button;
        this.gestureConnectionService = gestureConnectionService;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        Command command;
        IGestureRecognitionService recognitionService = gestureConnectionService.getRecognitionService();

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

package com.salve.activities.commands;

import com.salve.agrf.gestures.IGestureRecognitionService;

/**
 * Created by Vlad on 7/18/2015.
 */
public class CommandFactory {

    public static Command create(CommandType commandType, IGestureRecognitionService recognitionService) {
        switch (commandType) {
            case StartTraining:
                return new StartTrainingCommand(recognitionService);
            case StopTraining:
                return new StopTrainingCommand(recognitionService);
            default:
                return null;
        }
    }
}

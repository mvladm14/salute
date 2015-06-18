package com.salve.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by dmuresan on 6/18/2015.
 */
public class ConnectionTest {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;

    /// Source of inspiration: http://stackoverflow.com/questions/2660968/how-to-prevent-android-bluetooth-rfcomm-connection-from-dying-immediately-after
    /// Should be able to send some dummy data over a bluetooth connection between two endpoints (NOT TESTED YET!!!)
    /// example usage: new ConntectionTest().simpleCommunication(someIntegerPortNumber)
    public void simpleCommunication(Integer portNumber)
    {
        Log.d(this.toString(), "Port: " + portNumber);

        try {
            hackAccessToCreateRfcommSocket(bluetoothDevice, portNumber);

            // attempt socket connection
            if (bluetoothSocket != null)
                bluetoothSocket.connect();

            sendDummyDataToConnectedDevice();
            sendDummyModelToConnectedDevice(createNewDummyModel("someUser", "071211121"));
        }
        catch (Exception ex)
        {
            Log.e(this.toString(), "Something went wrong: \n" + ex.getStackTrace());
        }
    }

    private void hackAccessToCreateRfcommSocket(BluetoothDevice bluetoothDevice, int portNumber) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // hack to access "createRfcommSocket" (reflection shit)
        Method m = bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});

        bluetoothSocket = (BluetoothSocket) m.invoke(bluetoothDevice, portNumber);

        if (bluetoothSocket == null) {
            Log.e(this.toString(), "Socket is null");
            return;
        }
    }

    private void sendDummyDataToConnectedDevice() throws IOException
    {
        try {
            OutputStream outputStream = bluetoothSocket.getOutputStream();

            String dummyData = "Some dummy data to send";

            outputStream.write(dummyData.getBytes());
        }
        catch (Exception ex) {
            Log.e(this.toString(), "Something went wrong while sending the dummy data: /n" + ex.getStackTrace());
        }
        finally {
            bluetoothSocket.close();
        }
    }

    private void sendDummyModelToConnectedDevice(DummyUserModel dummyModel) throws IOException
    {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(null);
        try {
            objectOutputStream.writeObject(dummyModel);
            objectOutputStream.close();
        }
        catch (Exception ex) {
            Log.e(this.toString(), "Something went wrong while sending the serialized model data: /n" + ex.getStackTrace());
        }
        finally {
            objectOutputStream.close();
        }
    }

    private DummyUserModel createNewDummyModel(String userName, String phoneNumber)
    {
        DummyUserModel um = new DummyUserModel();
        um.setUserId(1);
        um.setUserName(userName);
        um.setPhoneNumber(phoneNumber);
        return um;
    }

    private void setUpAdapter()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) return;

        bluetoothDevice = bluetoothAdapter.getRemoteDevice("00:00:00:00:00:00");

        Log.e(this.toString(), "Bluetooth not avaialable!");
    }


    private class DummyUserModel implements Serializable
    {
        private int userId;
        private String userName;
        private String phoneNumber;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}

package com.huntercollab.app.network.loopjtasks.realtime;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RMSProtocol {

    //Wrapper for rawWriteMessage
    public static void writeUTF(String msg, DataOutputStream dout) throws IOException {
        RMSProtocol.rawWriteMessage(dout, msg);
    }

    //Construct a string from the byte message and return
    public static String readUTF(DataInputStream dis) throws IOException {
        return new String(RMSProtocol.rawReadMessage(dis));
    }

    //First write 4 bytes which is the length of the message
    //Then write the message itself
    public static void rawWriteMessage(DataOutputStream dout, String message) throws IOException {
        dout.writeInt(message.length());
        dout.writeBytes(message);
    }

    //Read first 4 bytes which is the length of the message = n
    //Then read the next n bytes which is the actual message.
    public static byte[] rawReadMessage(DataInputStream dis) throws IOException {
        int length = dis.readInt();
        return readN(dis, length);
    }

    //Reads n bytes from the socket's stream.
    public static byte[] readN(DataInputStream dis, int n) throws IOException {
        byte[] data = new byte[n];
        for (int i = 0; i < n; i++) {
            data[i] = dis.readByte();
        }
        return data;
    }
}

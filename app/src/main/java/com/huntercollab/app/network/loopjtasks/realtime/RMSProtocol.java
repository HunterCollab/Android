package com.huntercollab.app.network.loopjtasks.realtime;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RMSProtocol {

    //@author: Hugh Leow & Ram Vakada
    //@brief: wrapper for 'rawWriteMessage'
    //@params: [String msg] [DataOutputStream dout]
    public static void writeUTF(String msg, DataOutputStream dout) throws IOException {
        RMSProtocol.rawWriteMessage(dout, msg);
    }

    //@author: Hugh Leow & Ram Vakada
    //@brief: Construct a string from the byte message and return
    //@params: [DataInputStream dis]
    //@return: String of message converted from byte
    public static String readUTF(DataInputStream dis) throws IOException {
        return new String(RMSProtocol.rawReadMessage(dis));
    }

    //@author: Hugh Leow & Ram Vakada
    //@brief:
    //First write 4 bytes which is the length of the message
    //Then write the message itself
    //@params: [DataOutputStream dout] [String message]
    public static void rawWriteMessage(DataOutputStream dout, String message) throws IOException {
        dout.writeInt(message.length());
        dout.writeBytes(message);
    }

    //@author: Hugh Leow & Ram Vakada
    //@brief:
    //Read first 4 bytes which is the length of the message = n
    //Then read the next n bytes which is the actual message
    //@params: [DataInputStream dis]
    //@return: byte message
    public static byte[] rawReadMessage(DataInputStream dis) throws IOException {
        int length = dis.readInt();
        return readN(dis, length);
    }

    //@author: Hugh Leow & Ram Vakada
    //@brief: Reads n bytes from the socket's stream
    //@params: [DataInputStream dis] [int n]
    //@return: array of bytes for message
    public static byte[] readN(DataInputStream dis, int n) throws IOException {
        byte[] data = new byte[n];
        for (int i = 0; i < n; i++) {
            data[i] = dis.readByte();
        }
        return data;
    }
}

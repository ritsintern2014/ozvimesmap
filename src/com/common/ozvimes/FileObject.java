package com.common.ozvimes;

public class FileObject {
    private String name;
    byte [] bufferArray = null;

    public FileObject(String name, byte[] data) {
        this.name = name;
        this.bufferArray = data;
    }

    public FileObject(String name) {
        this.name = name;
        }

    public void appendData(byte[] data, int numberOfBytes) {

        if (bufferArray == null)
        {
            this.bufferArray = new byte [numberOfBytes];
            System.arraycopy(data, 0, bufferArray, 0, numberOfBytes);
        }
        else
        {
            byte[] tempArray = new byte[bufferArray.length + numberOfBytes];
            System.arraycopy(bufferArray, 0, tempArray, 0, bufferArray.length);
            System.arraycopy(data, 0, tempArray, bufferArray.length, numberOfBytes);    
            bufferArray = tempArray;
        }
    }

    public byte[] getData() {
        return bufferArray;
    }

    public void setData(byte[] data) {
        this.bufferArray = data;
    }

    public String getName() {
        return name;
    }
}

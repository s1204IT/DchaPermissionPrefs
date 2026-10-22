package jp.co.benesse.touch.comm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IHttpResponse extends IInterface {
    void doProcess() throws RemoteException;

    void onError(String str, String str2, int i, String str3, int i2) throws RemoteException;

    void onProgress(long j, long j2) throws RemoteException;

    void setResponseString(String str) throws RemoteException;

    public static abstract class Stub extends Binder implements IHttpResponse {
        private static final String DESCRIPTOR = "jp.co.benesse.touch.comm.IHttpResponse";
        static final int TRANSACTION_doProcess = 2;
        static final int TRANSACTION_onError = 3;
        static final int TRANSACTION_onProgress = 4;
        static final int TRANSACTION_setResponseString = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IHttpResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IHttpResponse)) {
                return (IHttpResponse) iin;
            }
            return new Proxy(obj);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    setResponseString(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    doProcess();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    int _arg3 = data.readInt();
                    String _arg4 = data.readString();
                    int _arg5 = data.readInt();
                    onError(_arg1, _arg2, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg6 = data.readLong();
                    long _arg7 = data.readLong();
                    onProgress(_arg6, _arg7);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IHttpResponse {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override
            public void setResponseString(String resString) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(resString);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void doProcess() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void onError(String reqID, String uri, int errCode, String errMsg, int statusCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reqID);
                    _data.writeString(uri);
                    _data.writeInt(errCode);
                    _data.writeString(errMsg);
                    _data.writeInt(statusCode);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void onProgress(long currentSize, long fullSize) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(currentSize);
                    _data.writeLong(fullSize);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}

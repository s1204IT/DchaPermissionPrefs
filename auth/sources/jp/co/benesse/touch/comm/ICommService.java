package jp.co.benesse.touch.comm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICommService extends IInterface {
    void requestComm2AuthServer(IHttpRequest iHttpRequest, IHttpResponse iHttpResponse) throws RemoteException;

    void requestComm2BaeServer(IHttpRequest iHttpRequest, IHttpResponse iHttpResponse) throws RemoteException;

    void requestComm2HttpServer(IHttpRequest iHttpRequest, IHttpResponse iHttpResponse) throws RemoteException;

    void requestComm2MemberServer(IHttpRequest iHttpRequest, IHttpResponse iHttpResponse) throws RemoteException;

    void setServerTime() throws RemoteException;

    public static abstract class Stub extends Binder implements ICommService {
        private static final String DESCRIPTOR = "jp.co.benesse.touch.comm.ICommService";
        static final int TRANSACTION_requestComm2AuthServer = 2;
        static final int TRANSACTION_requestComm2BaeServer = 1;
        static final int TRANSACTION_requestComm2HttpServer = 4;
        static final int TRANSACTION_requestComm2MemberServer = 3;
        static final int TRANSACTION_setServerTime = 5;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICommService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ICommService)) {
                return (ICommService) iin;
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
                    IHttpRequest _arg0 = IHttpRequest.Stub.asInterface(data.readStrongBinder());
                    IHttpResponse _arg1 = IHttpResponse.Stub.asInterface(data.readStrongBinder());
                    requestComm2BaeServer(_arg0, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IHttpRequest _arg2 = IHttpRequest.Stub.asInterface(data.readStrongBinder());
                    IHttpResponse _arg3 = IHttpResponse.Stub.asInterface(data.readStrongBinder());
                    requestComm2AuthServer(_arg2, _arg3);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IHttpRequest _arg4 = IHttpRequest.Stub.asInterface(data.readStrongBinder());
                    IHttpResponse _arg5 = IHttpResponse.Stub.asInterface(data.readStrongBinder());
                    requestComm2MemberServer(_arg4, _arg5);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IHttpRequest _arg6 = IHttpRequest.Stub.asInterface(data.readStrongBinder());
                    IHttpResponse _arg7 = IHttpResponse.Stub.asInterface(data.readStrongBinder());
                    requestComm2HttpServer(_arg6, _arg7);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    setServerTime();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ICommService {
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
            public void requestComm2BaeServer(IHttpRequest req, IHttpResponse res) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(req != null ? req.asBinder() : null);
                    _data.writeStrongBinder(res != null ? res.asBinder() : null);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override
            public void requestComm2AuthServer(IHttpRequest req, IHttpResponse res) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(req != null ? req.asBinder() : null);
                    _data.writeStrongBinder(res != null ? res.asBinder() : null);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override
            public void requestComm2MemberServer(IHttpRequest req, IHttpResponse res) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(req != null ? req.asBinder() : null);
                    _data.writeStrongBinder(res != null ? res.asBinder() : null);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override
            public void requestComm2HttpServer(IHttpRequest req, IHttpResponse res) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(req != null ? req.asBinder() : null);
                    _data.writeStrongBinder(res != null ? res.asBinder() : null);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override
            public void setServerTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}

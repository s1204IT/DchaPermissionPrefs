package jp.co.benesse.touch.auth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IAuthService extends IInterface {
    void authUser(String str, String str2) throws RemoteException;

    boolean deleteAuthInfo() throws RemoteException;

    void getAkamaiToken(String str, List<String> list, IAuthCallback iAuthCallback) throws RemoteException;

    String getAppIdentifier() throws RemoteException;

    String getArrayValues(String str) throws RemoteException;

    String getHttpsSessionKey() throws RemoteException;

    void getOneTimeKey(IAuthCallback iAuthCallback) throws RemoteException;

    String getSessionHeaderName(boolean z) throws RemoteException;

    void getSessionKey(boolean z, IAuthCallback iAuthCallback) throws RemoteException;

    String getStringValue(String str) throws RemoteException;

    String getUserId() throws RemoteException;

    boolean lockAuthWorking() throws RemoteException;

    void setArrayValues(String str, String str2) throws RemoteException;

    void setStringValue(String str, String str2) throws RemoteException;

    void updateContractInfo(IAuthCallback iAuthCallback) throws RemoteException;

    void updateSessionKey(boolean z, IAuthCallback iAuthCallback) throws RemoteException;

    public static abstract class Stub extends Binder implements IAuthService {
        private static final String DESCRIPTOR = "jp.co.benesse.touch.auth.IAuthService";
        static final int TRANSACTION_authUser = 1;
        static final int TRANSACTION_deleteAuthInfo = 9;
        static final int TRANSACTION_getAkamaiToken = 7;
        static final int TRANSACTION_getAppIdentifier = 14;
        static final int TRANSACTION_getArrayValues = 12;
        static final int TRANSACTION_getHttpsSessionKey = 16;
        static final int TRANSACTION_getOneTimeKey = 6;
        static final int TRANSACTION_getSessionHeaderName = 4;
        static final int TRANSACTION_getSessionKey = 3;
        static final int TRANSACTION_getStringValue = 10;
        static final int TRANSACTION_getUserId = 15;
        static final int TRANSACTION_lockAuthWorking = 8;
        static final int TRANSACTION_setArrayValues = 13;
        static final int TRANSACTION_setStringValue = 11;
        static final int TRANSACTION_updateContractInfo = 2;
        static final int TRANSACTION_updateSessionKey = 5;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAuthService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAuthService)) {
                return (IAuthService) iin;
            }
            return new Proxy(obj);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    authUser(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateContractInfo(IAuthCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    getSessionKey(parcel.readInt() != 0, IAuthCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    String sessionHeaderName = getSessionHeaderName(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeString(sessionHeaderName);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateSessionKey(parcel.readInt() != 0, IAuthCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    getOneTimeKey(IAuthCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_getAkamaiToken:
                    parcel.enforceInterface(DESCRIPTOR);
                    getAkamaiToken(parcel.readString(), parcel.createStringArrayList(), IAuthCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_lockAuthWorking:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zLockAuthWorking = lockAuthWorking();
                    parcel2.writeNoException();
                    parcel2.writeInt(zLockAuthWorking ? 1 : 0);
                    return true;
                case TRANSACTION_deleteAuthInfo:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zDeleteAuthInfo = deleteAuthInfo();
                    parcel2.writeNoException();
                    parcel2.writeInt(zDeleteAuthInfo ? 1 : 0);
                    return true;
                case TRANSACTION_getStringValue:
                    parcel.enforceInterface(DESCRIPTOR);
                    String stringValue = getStringValue(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(stringValue);
                    return true;
                case TRANSACTION_setStringValue:
                    parcel.enforceInterface(DESCRIPTOR);
                    setStringValue(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_getArrayValues:
                    parcel.enforceInterface(DESCRIPTOR);
                    String arrayValues = getArrayValues(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(arrayValues);
                    return true;
                case TRANSACTION_setArrayValues:
                    parcel.enforceInterface(DESCRIPTOR);
                    setArrayValues(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_getAppIdentifier:
                    parcel.enforceInterface(DESCRIPTOR);
                    String appIdentifier = getAppIdentifier();
                    parcel2.writeNoException();
                    parcel2.writeString(appIdentifier);
                    return true;
                case TRANSACTION_getUserId:
                    parcel.enforceInterface(DESCRIPTOR);
                    String userId = getUserId();
                    parcel2.writeNoException();
                    parcel2.writeString(userId);
                    return true;
                case TRANSACTION_getHttpsSessionKey:
                    parcel.enforceInterface(DESCRIPTOR);
                    String httpsSessionKey = getHttpsSessionKey();
                    parcel2.writeNoException();
                    parcel2.writeString(httpsSessionKey);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        private static class Proxy implements IAuthService {
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
            public void authUser(String userId, String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(userId);
                    _data.writeString(password);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void updateContractInfo(IAuthCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void getSessionKey(boolean secure, IAuthCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(secure ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public String getSessionHeaderName(boolean secure) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(secure ? 1 : 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void updateSessionKey(boolean secure, IAuthCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(secure ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void getOneTimeKey(IAuthCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void getAkamaiToken(String url, List<String> accessList, IAuthCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(url);
                    _data.writeStringList(accessList);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_getAkamaiToken, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public boolean lockAuthWorking() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_lockAuthWorking, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public boolean deleteAuthInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_deleteAuthInfo, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public String getStringValue(String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    this.mRemote.transact(Stub.TRANSACTION_getStringValue, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void setStringValue(String key, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(value);
                    this.mRemote.transact(Stub.TRANSACTION_setStringValue, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public String getArrayValues(String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    this.mRemote.transact(Stub.TRANSACTION_getArrayValues, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void setArrayValues(String key, String values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(values);
                    this.mRemote.transact(Stub.TRANSACTION_setArrayValues, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public String getAppIdentifier() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getAppIdentifier, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public String getUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getUserId, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public String getHttpsSessionKey() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getHttpsSessionKey, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}

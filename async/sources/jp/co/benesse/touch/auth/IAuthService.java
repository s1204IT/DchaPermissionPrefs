package jp.co.benesse.touch.auth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IAuthService extends IInterface {

    public static class Default implements IAuthService {
        @Override
        public IBinder asBinder() {
            return null;
        }

        @Override
        public void authUser(String str, String str2) throws RemoteException {
        }

        @Override
        public boolean deleteAuthInfo() throws RemoteException {
            return false;
        }

        @Override
        public void getAkamaiToken(String str, List<String> list, IAuthCallback iAuthCallback) throws RemoteException {
        }

        @Override
        public String getAppIdentifier() throws RemoteException {
            return null;
        }

        @Override
        public String getArrayValues(String str) throws RemoteException {
            return null;
        }

        @Override
        public String getHttpsSessionKey() throws RemoteException {
            return null;
        }

        @Override
        public void getOneTimeKey(IAuthCallback iAuthCallback) throws RemoteException {
        }

        @Override
        public String getSessionHeaderName(boolean z) throws RemoteException {
            return null;
        }

        @Override
        public void getSessionKey(boolean z, IAuthCallback iAuthCallback) throws RemoteException {
        }

        @Override
        public String getStringValue(String str) throws RemoteException {
            return null;
        }

        @Override
        public String getUserId() throws RemoteException {
            return null;
        }

        @Override
        public boolean lockAuthWorking() throws RemoteException {
            return false;
        }

        @Override
        public void setArrayValues(String str, String str2) throws RemoteException {
        }

        @Override
        public void setStringValue(String str, String str2) throws RemoteException {
        }

        @Override
        public void updateContractInfo(IAuthCallback iAuthCallback) throws RemoteException {
        }

        @Override
        public void updateSessionKey(boolean z, IAuthCallback iAuthCallback) throws RemoteException {
        }
    }

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

        @Override
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAuthService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IAuthService)) {
                return (IAuthService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
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
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    getAkamaiToken(parcel.readString(), parcel.createStringArrayList(), IAuthCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zLockAuthWorking = lockAuthWorking();
                    parcel2.writeNoException();
                    parcel2.writeInt(zLockAuthWorking ? 1 : 0);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zDeleteAuthInfo = deleteAuthInfo();
                    parcel2.writeNoException();
                    parcel2.writeInt(zDeleteAuthInfo ? 1 : 0);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    String stringValue = getStringValue(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(stringValue);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    setStringValue(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    String arrayValues = getArrayValues(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(arrayValues);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    setArrayValues(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    String appIdentifier = getAppIdentifier();
                    parcel2.writeNoException();
                    parcel2.writeString(appIdentifier);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    String userId = getUserId();
                    parcel2.writeNoException();
                    parcel2.writeString(userId);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    String httpsSessionKey = getHttpsSessionKey();
                    parcel2.writeNoException();
                    parcel2.writeString(httpsSessionKey);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        private static class Proxy implements IAuthService {
            public static IAuthService sDefaultImpl;
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override
            public void authUser(String str, String str2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    if (!this.mRemote.transact(1, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().authUser(str, str2);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public void updateContractInfo(IAuthCallback iAuthCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeStrongBinder(iAuthCallback != null ? iAuthCallback.asBinder() : null);
                    if (!this.mRemote.transact(2, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateContractInfo(iAuthCallback);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public void getSessionKey(boolean z, IAuthCallback iAuthCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeStrongBinder(iAuthCallback != null ? iAuthCallback.asBinder() : null);
                    if (!this.mRemote.transact(3, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getSessionKey(z, iAuthCallback);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public String getSessionHeaderName(boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(z ? 1 : 0);
                    if (!this.mRemote.transact(4, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSessionHeaderName(z);
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public void updateSessionKey(boolean z, IAuthCallback iAuthCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(z ? 1 : 0);
                    parcelObtain.writeStrongBinder(iAuthCallback != null ? iAuthCallback.asBinder() : null);
                    if (!this.mRemote.transact(5, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateSessionKey(z, iAuthCallback);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public void getOneTimeKey(IAuthCallback iAuthCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeStrongBinder(iAuthCallback != null ? iAuthCallback.asBinder() : null);
                    if (!this.mRemote.transact(6, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getOneTimeKey(iAuthCallback);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public void getAkamaiToken(String str, List<String> list, IAuthCallback iAuthCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeStringList(list);
                    parcelObtain.writeStrongBinder(iAuthCallback != null ? iAuthCallback.asBinder() : null);
                    if (!this.mRemote.transact(7, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getAkamaiToken(str, list, iAuthCallback);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public boolean lockAuthWorking() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(8, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().lockAuthWorking();
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public boolean deleteAuthInfo() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(9, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().deleteAuthInfo();
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public String getStringValue(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(10, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStringValue(str);
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public void setStringValue(String str, String str2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    if (!this.mRemote.transact(11, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setStringValue(str, str2);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public String getArrayValues(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(12, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getArrayValues(str);
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public void setArrayValues(String str, String str2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    if (!this.mRemote.transact(13, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setArrayValues(str, str2);
                    } else {
                        parcelObtain2.readException();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public String getAppIdentifier() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(14, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAppIdentifier();
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public String getUserId() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(15, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUserId();
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override
            public String getHttpsSessionKey() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(16, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getHttpsSessionKey();
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IAuthService iAuthService) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (iAuthService == null) {
                return false;
            }
            Proxy.sDefaultImpl = iAuthService;
            return true;
        }

        public static IAuthService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}

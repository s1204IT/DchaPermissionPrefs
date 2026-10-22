package jp.co.benesse.dcha.dchaservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDchaService extends IInterface {
    void cancelSetup() throws RemoteException;

    boolean checkPadRooted() throws RemoteException;

    void clearDefaultPreferredApp(String str) throws RemoteException;

    boolean copyFile(String str, String str2) throws RemoteException;

    boolean copyUpdateImage(String str, String str2) throws RemoteException;

    boolean deleteFile(String str) throws RemoteException;

    void disableADB() throws RemoteException;

    String getForegroundPackageName() throws RemoteException;

    int getSetupStatus() throws RemoteException;

    int getUserCount() throws RemoteException;

    void hideNavigationBar(boolean z) throws RemoteException;

    boolean installApp(String str, int i) throws RemoteException;

    boolean isDeviceEncryptionEnabled() throws RemoteException;

    void rebootPad(int i, String str) throws RemoteException;

    void removeTask(String str) throws RemoteException;

    void sdUnmount() throws RemoteException;

    void setDefaultParam() throws RemoteException;

    void setDefaultPreferredHomeApp(String str) throws RemoteException;

    void setPermissionEnforced(boolean z) throws RemoteException;

    void setSetupStatus(int i) throws RemoteException;

    void setSystemTime(String str, String str2) throws RemoteException;

    boolean uninstallApp(String str, int i) throws RemoteException;

    boolean verifyUpdateImage(String str) throws RemoteException;

    public static abstract class Stub extends Binder implements IDchaService {
        private static final String DESCRIPTOR = "jp.co.benesse.dcha.dchaservice.IDchaService";
        static final int TRANSACTION_cancelSetup = 10;
        static final int TRANSACTION_checkPadRooted = 7;
        static final int TRANSACTION_clearDefaultPreferredApp = 5;
        static final int TRANSACTION_copyFile = 18;
        static final int TRANSACTION_copyUpdateImage = 2;
        static final int TRANSACTION_deleteFile = 19;
        static final int TRANSACTION_disableADB = 6;
        static final int TRANSACTION_getForegroundPackageName = 17;
        static final int TRANSACTION_getSetupStatus = 12;
        static final int TRANSACTION_getUserCount = 20;
        static final int TRANSACTION_hideNavigationBar = 22;
        static final int TRANSACTION_installApp = 8;
        static final int TRANSACTION_isDeviceEncryptionEnabled = 21;
        static final int TRANSACTION_rebootPad = 3;
        static final int TRANSACTION_removeTask = 14;
        static final int TRANSACTION_sdUnmount = 15;
        static final int TRANSACTION_setDefaultParam = 16;
        static final int TRANSACTION_setDefaultPreferredHomeApp = 4;
        static final int TRANSACTION_setPermissionEnforced = 23;
        static final int TRANSACTION_setSetupStatus = 11;
        static final int TRANSACTION_setSystemTime = 13;
        static final int TRANSACTION_uninstallApp = 9;
        static final int TRANSACTION_verifyUpdateImage = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDchaService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IDchaService)) {
                return (IDchaService) iin;
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
                    boolean _result = verifyUpdateImage(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    boolean _result2 = copyUpdateImage(_arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg3 = data.readInt();
                    String _arg4 = data.readString();
                    rebootPad(_arg3, _arg4);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg5 = data.readString();
                    setDefaultPreferredHomeApp(_arg5);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg6 = data.readString();
                    clearDefaultPreferredApp(_arg6);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_disableADB:
                    data.enforceInterface(DESCRIPTOR);
                    disableADB();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_checkPadRooted:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result3 = checkPadRooted();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case TRANSACTION_installApp:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg7 = data.readString();
                    int _arg8 = data.readInt();
                    boolean _result4 = installApp(_arg7, _arg8);
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case TRANSACTION_uninstallApp:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg9 = data.readString();
                    int _arg10 = data.readInt();
                    boolean _result5 = uninstallApp(_arg9, _arg10);
                    reply.writeNoException();
                    reply.writeInt(_result5 ? 1 : 0);
                    return true;
                case TRANSACTION_cancelSetup:
                    data.enforceInterface(DESCRIPTOR);
                    cancelSetup();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setSetupStatus:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg11 = data.readInt();
                    setSetupStatus(_arg11);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getSetupStatus:
                    data.enforceInterface(DESCRIPTOR);
                    int _result6 = getSetupStatus();
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case TRANSACTION_setSystemTime:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg12 = data.readString();
                    String _arg13 = data.readString();
                    setSystemTime(_arg12, _arg13);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_removeTask:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg14 = data.readString();
                    removeTask(_arg14);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_sdUnmount:
                    data.enforceInterface(DESCRIPTOR);
                    sdUnmount();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setDefaultParam:
                    data.enforceInterface(DESCRIPTOR);
                    setDefaultParam();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getForegroundPackageName:
                    data.enforceInterface(DESCRIPTOR);
                    String _result7 = getForegroundPackageName();
                    reply.writeNoException();
                    reply.writeString(_result7);
                    return true;
                case TRANSACTION_copyFile:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg15 = data.readString();
                    String _arg16 = data.readString();
                    boolean _result8 = copyFile(_arg15, _arg16);
                    reply.writeNoException();
                    reply.writeInt(_result8 ? 1 : 0);
                    return true;
                case TRANSACTION_deleteFile:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg17 = data.readString();
                    boolean _result9 = deleteFile(_arg17);
                    reply.writeNoException();
                    reply.writeInt(_result9 ? 1 : 0);
                    return true;
                case TRANSACTION_getUserCount:
                    data.enforceInterface(DESCRIPTOR);
                    int _result10 = getUserCount();
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case TRANSACTION_isDeviceEncryptionEnabled:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result11 = isDeviceEncryptionEnabled();
                    reply.writeNoException();
                    reply.writeInt(_result11 ? 1 : 0);
                    return true;
                case TRANSACTION_hideNavigationBar:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg18 = data.readInt() != 0;
                    hideNavigationBar(_arg18);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setPermissionEnforced:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg19 = data.readInt() != 0;
                    setPermissionEnforced(_arg19);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IDchaService {
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
            public boolean verifyUpdateImage(String updateFile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(updateFile);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public boolean copyUpdateImage(String srcFile, String dstFile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(srcFile);
                    _data.writeString(dstFile);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void rebootPad(int rebootMode, String srcFile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rebootMode);
                    _data.writeString(srcFile);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override
            public void setDefaultPreferredHomeApp(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void clearDefaultPreferredApp(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void disableADB() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_disableADB, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public boolean checkPadRooted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_checkPadRooted, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public boolean installApp(String path, int installFlag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(installFlag);
                    this.mRemote.transact(Stub.TRANSACTION_installApp, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public boolean uninstallApp(String packageName, int uninstallFlag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(uninstallFlag);
                    this.mRemote.transact(Stub.TRANSACTION_uninstallApp, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void cancelSetup() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_cancelSetup, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void setSetupStatus(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    this.mRemote.transact(Stub.TRANSACTION_setSetupStatus, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public int getSetupStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSetupStatus, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void setSystemTime(String time, String timeFormat) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(time);
                    _data.writeString(timeFormat);
                    this.mRemote.transact(Stub.TRANSACTION_setSystemTime, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void removeTask(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(Stub.TRANSACTION_removeTask, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void sdUnmount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_sdUnmount, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void setDefaultParam() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_setDefaultParam, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public String getForegroundPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getForegroundPackageName, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public boolean copyFile(String srcFilePath, String dstFilePath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(srcFilePath);
                    _data.writeString(dstFilePath);
                    this.mRemote.transact(Stub.TRANSACTION_copyFile, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public boolean deleteFile(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    this.mRemote.transact(Stub.TRANSACTION_deleteFile, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public int getUserCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getUserCount, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public boolean isDeviceEncryptionEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isDeviceEncryptionEnabled, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void hideNavigationBar(boolean hide) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hide ? 1 : 0);
                    this.mRemote.transact(Stub.TRANSACTION_hideNavigationBar, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void setPermissionEnforced(boolean enforced) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enforced ? 1 : 0);
                    this.mRemote.transact(Stub.TRANSACTION_setPermissionEnforced, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}

package jp.co.benesse.dcha.util;

import android.content.Context;
import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

public class FileUtils {
    private static final String TEMP_FILE_NAME_INDEX_SEPARATOR = "_";
    private static final String TMP_FILE_SUFFIX = ".tmp";

    private FileUtils() {
    }

    public static final boolean close(Closeable closeable) {
        if (closeable == null) {
            return true;
        }
        try {
            closeable.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static final File openLocalFile(Context context, String path) {
        File fileDir = context.getFilesDir();
        int index = path.lastIndexOf("/");
        if (index > -1) {
            File subDir = new File(fileDir, path.substring(0, index));
            if (!subDir.exists() && !subDir.mkdirs()) {
                return null;
            }
        }
        return new File(fileDir, path);
    }

    public static final String getLocalFilePath(Context context, String path) {
        File filedir = openLocalFile(context, path);
        if (filedir == null) {
            return null;
        }
        return filedir.getPath();
    }

    public static final boolean canReadFile(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return file.isFile() && file.canRead();
        }
        return true;
    }

    public static final boolean canWriteFile(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return file.isFile() && file.canWrite();
        }
        return true;
    }

    public static final File[] getWriteFiles(String[] paths) {
        if (ArrayUtils.isEmpty(paths)) {
            return null;
        }
        File[] files = new File[paths.length];
        for (int i = 0; i < paths.length; i++) {
            File file = getWriteFile(paths[i]);
            if (file == null) {
                return null;
            }
            files[i] = file;
        }
        return files;
    }

    public static final File getWriteFile(String path) {
        File file = new File(path);
        if (!canWriteFile(file)) {
            return null;
        }
        return file;
    }

    public static final File createTempFile(Context context, String prefix) {
        File file = createTempFile(context, prefix, null);
        return file;
    }

    public static final File createTempFile(Context context, String prefix, String suffix) {
        if (context == null || TextUtils.isEmpty(prefix)) {
            return null;
        }
        String fileSuffix = suffix;
        if (TextUtils.isEmpty(suffix)) {
            fileSuffix = TMP_FILE_SUFFIX;
        }
        StringBuffer buf = new StringBuffer();
        buf.append(prefix);
        buf.append(fileSuffix);
        String filename = buf.toString();
        File tmpFile = new File(context.getFilesDir(), filename);
        try {
            if (!tmpFile.exists()) {
                tmpFile.createNewFile();
                return tmpFile;
            }
            return tmpFile;
        } catch (IOException e) {
            return null;
        }
    }

    public static final File[] createTempFiles(Context context, int size) {
        if (context != null && size >= 0) {
            File[] files = new File[size];
            long now = System.nanoTime();
            StringBuffer buf = new StringBuffer();
            for (int index = 0; index < size; index++) {
                buf.setLength(0);
                buf.append(now);
                buf.append(TEMP_FILE_NAME_INDEX_SEPARATOR);
                buf.append(index);
                String prefix = buf.toString();
                File file = createTempFile(context, prefix);
                if (file == null) {
                    return null;
                }
                files[index] = file;
            }
            return files;
        }
        return null;
    }

    public static final boolean deleteTempFile(Context context, File file) {
        if (context == null || !canWriteFile(file)) {
            return false;
        }
        String name = file.getName();
        return context.deleteFile(name);
    }

    public static final boolean deleteTempFiles(Context context, File[] files) {
        if (context == null || ArrayUtils.isEmpty(files)) {
            return false;
        }
        boolean result = true;
        for (File file : files) {
            result = result && deleteTempFile(context, file);
        }
        return result;
    }

    public static final boolean deleteFile(File file) {
        if (canWriteFile(file)) {
            return file.delete();
        }
        return false;
    }

    public static final boolean deleteFiles(File[] files) {
        if (ArrayUtils.isEmpty(files)) {
            return false;
        }
        boolean result = true;
        for (File file : files) {
            result = result && deleteFile(file);
        }
        return result;
    }

    public static final boolean serializeObject(Context context, String fileName, int mode, Serializable serializable) throws Throwable {
        FileOutputStream fileStream = null;
        ObjectOutputStream objectStream = null;
        if (context == null || fileName == null || serializable == null) {
            return false;
        }
        try {
            fileStream = context.openFileOutput(fileName, mode);
            ObjectOutputStream objectStream2 = new ObjectOutputStream(fileStream);
            try {
                objectStream2.writeObject(serializable);
                close(objectStream2);
                close(fileStream);
                return true;
            } catch (FileNotFoundException e) {
                objectStream = objectStream2;
                close(objectStream);
                close(fileStream);
                return false;
            } catch (IOException e2) {
                objectStream = objectStream2;
                close(objectStream);
                close(fileStream);
                return false;
            } catch (Throwable th) {
                th = th;
                objectStream = objectStream2;
                close(objectStream);
                close(fileStream);
                throw th;
            }
        } catch (FileNotFoundException e3) {
        } catch (IOException e4) {
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static final boolean serializeObject(Context context, String fileName, Serializable serializable) {
        return serializeObject(context, fileName, 0, serializable);
    }

    public static final Object deserializeObject(Context context, String fileName, boolean fileDeleted) throws Throwable {
        FileInputStream fileStream = null;
        ObjectInputStream objectStream = null;
        if (context != null && fileName != null) {
            try {
                fileStream = context.openFileInput(fileName);
                ObjectInputStream objectStream2 = new ObjectInputStream(fileStream);
                try {
                    Object object = objectStream2.readObject();
                    close(objectStream2);
                    close(fileStream);
                    if (fileDeleted) {
                        context.deleteFile(fileName);
                    }
                    return object;
                } catch (FileNotFoundException e) {
                    objectStream = objectStream2;
                    close(objectStream);
                    close(fileStream);
                    if (fileDeleted) {
                        context.deleteFile(fileName);
                    }
                    return null;
                } catch (StreamCorruptedException e2) {
                    objectStream = objectStream2;
                    close(objectStream);
                    close(fileStream);
                    if (fileDeleted) {
                        context.deleteFile(fileName);
                    }
                    return null;
                } catch (IOException e3) {
                    objectStream = objectStream2;
                    close(objectStream);
                    close(fileStream);
                    if (fileDeleted) {
                        context.deleteFile(fileName);
                    }
                    return null;
                } catch (ClassNotFoundException e4) {
                    objectStream = objectStream2;
                    close(objectStream);
                    close(fileStream);
                    if (fileDeleted) {
                        context.deleteFile(fileName);
                    }
                    return null;
                } catch (Throwable th) {
                    th = th;
                    objectStream = objectStream2;
                    close(objectStream);
                    close(fileStream);
                    if (fileDeleted) {
                        context.deleteFile(fileName);
                    }
                    throw th;
                }
            } catch (FileNotFoundException e5) {
            } catch (StreamCorruptedException e6) {
            } catch (IOException e7) {
            } catch (ClassNotFoundException e8) {
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return false;
    }
}

package jp.co.benesse.touch.update.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class UpdateInstallInfoDto implements Parcelable {
    public static final Parcelable.Creator<UpdateInstallInfoDto> CREATOR = new Parcelable.Creator<UpdateInstallInfoDto>() {
        @Override
        public UpdateInstallInfoDto createFromParcel(Parcel in) {
            return new UpdateInstallInfoDto(in);
        }

        @Override
        public UpdateInstallInfoDto[] newArray(int size) {
            return new UpdateInstallInfoDto[size];
        }
    };
    private int number;
    private String path;
    private int type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeInt(this.type);
        dest.writeInt(this.number);
    }

    private UpdateInstallInfoDto(Parcel in) {
        this.path = in.readString();
        this.type = in.readInt();
        this.number = in.readInt();
    }

    public UpdateInstallInfoDto(String path, int type, int number) {
        this.path = path;
        this.type = type;
        this.number = number;
    }

    public final String getPath() {
        return this.path;
    }

    public final int getType() {
        return this.type;
    }

    public final int getNumber() {
        return this.number;
    }
}

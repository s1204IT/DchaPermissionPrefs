package jp.co.benesse.touch.update.dto;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class UpdateInstallInfoDtoList implements Parcelable {
    public static final Parcelable.Creator<UpdateInstallInfoDtoList> CREATOR = new Parcelable.Creator<UpdateInstallInfoDtoList>() {
        @Override
        public UpdateInstallInfoDtoList createFromParcel(Parcel in) {
            return new UpdateInstallInfoDtoList(in);
        }

        @Override
        public UpdateInstallInfoDtoList[] newArray(int size) {
            return new UpdateInstallInfoDtoList[size];
        }
    };
    private ArrayList<UpdateInstallInfoDto> installinfolist;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.installinfolist);
    }

    public ArrayList<UpdateInstallInfoDto> getInstallinfolist() {
        return this.installinfolist;
    }

    private UpdateInstallInfoDtoList(Parcel in) {
        this.installinfolist = in.createTypedArrayList(UpdateInstallInfoDto.CREATOR);
    }

    public UpdateInstallInfoDtoList(ArrayList<UpdateInstallInfoDto> installinfolist) {
        this.installinfolist = installinfolist;
    }
}

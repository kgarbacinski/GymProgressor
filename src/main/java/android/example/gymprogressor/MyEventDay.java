package android.example.gymprogressor;

import android.os.Parcel;
import android.os.Parcelable;


import com.applandeo.materialcalendarview.EventDay;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

class MyEventDay extends EventDay implements Parcelable {
    public static final Type LIST_TYPE_CALENDAR = new TypeToken<List<List<String>>>() {
    }.getType();
    public static final Creator<MyEventDay> CREATOR = new Creator<MyEventDay>() {
        @Override
        public MyEventDay createFromParcel(Parcel in) {
            return new MyEventDay(in);
        }

        @Override
        public MyEventDay[] newArray(int size) {
            return new MyEventDay[size];
        }
    };
    private String mNoteJSON;

    MyEventDay(Calendar day, int imageResource, String note) {
        super(day, imageResource);
        mNoteJSON = note;
    }

    private MyEventDay(Parcel in) {
        super((Calendar) in.readSerializable(), in.readInt());
        mNoteJSON = in.readString();
    }

    public String getNote() {
        return mNoteJSON;
    }

    void setNote(String editNote) {
        mNoteJSON = editNote;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(getCalendar());
        parcel.writeInt(getImageResource());
        parcel.writeString(mNoteJSON);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
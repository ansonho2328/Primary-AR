package hk.edu.ouhk.arprimary.viewmodel.armodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArViewModel extends ViewModel {

    private final MutableLiveData<UnitSection> unitSectionMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<UnitSection> getUnitSectionMutableLiveData() {
        return unitSectionMutableLiveData;
    }

}

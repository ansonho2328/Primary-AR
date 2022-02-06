package hk.edu.ouhk.arprimary.viewmodel.armodel.practice;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

@Deprecated
public class SectionViewModel extends ViewModel {

    private final MutableLiveData<UnitSection> unitSectionMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<UnitSection> getUnitSectionMutableLiveData() {
        return unitSectionMutableLiveData;
    }

}

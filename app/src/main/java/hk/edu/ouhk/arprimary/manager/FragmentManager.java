package hk.edu.ouhk.arprimary.manager;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import hk.edu.ouhk.arprimary.model.LessonFragment;

public abstract class FragmentManager<E> {

    private final Map<String, Map<Integer, E[]>> topicUnits = new ConcurrentHashMap<>();

    public Optional<E[]> getFragmentsByTopicUnit(String topic, int unit) {
        return Optional.ofNullable(topicUnits.get(topic)).map(frag -> frag.get(unit));
    }

    public void registerTopicUnit(String topic, int unit, E[] fragments) {
        topicUnits.putIfAbsent(topic, new ConcurrentHashMap<>());
        topicUnits.get(topic).put(unit, fragments);
    }

}

package somantic.library;

import static somantic.Main.MAX_WORDS_IN_REPOSITORY;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class SomanticWord implements Serializable, Comparable<SomanticWord> {

    public static Map<Integer, String> idStore = new HashMap<>();
    private Integer id;
    private String lemma = "";
    private Set<String> POS = new ConcurrentSkipListSet<>();
    private Set<String> words = new ConcurrentSkipListSet<>();
    private Set<SomanticWord> previous = new ConcurrentSkipListSet<>();
    private Set<SomanticWord> next = new ConcurrentSkipListSet<>();
    private Set<SomanticSentence> sentences = new ConcurrentSkipListSet<>();
    private Set<SomanticAffect> affects = new ConcurrentSkipListSet<>();
    private String description;
    private Set<String> tags = new ConcurrentSkipListSet<>();
    private static volatile int lastKnownIndex;

    public SomanticWord(String lemma) {
        this.lemma = lemma;
        getId();
    }

    synchronized public Set<String> getPOS() {
        return POS;
    }

    synchronized public void setPOS(Set<String> POS) {
        this.POS = POS;
    }

    synchronized public void addPOS(String POS) {
        this.POS.add(POS);
    }

    synchronized public String getLemma() {
        return lemma;
    }

    synchronized public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public Integer getId() {
        if (id == null) {
            id = generateId();
        }
        return id;
    }

    public void setId(Integer id) {
        this.id = Math.abs(id);
    }

    private boolean placedIdInStore(Integer id) {
        id = Math.abs(id);
        String lem = idStore.get(id);
        if (lem == null) {
            idStore.put(id, lemma);
            return true;
        }
        if (lem.equals(lemma)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return lemma;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.lemma);
    }

    public int generateId() {
        int hc = Math.abs(hashCode() % MAX_WORDS_IN_REPOSITORY);
        if (placedIdInStore(hc)) {
            return hc;
        }
        for (int i = ++lastKnownIndex; i < MAX_WORDS_IN_REPOSITORY; i++) {
            if (placedIdInStore(i)) {
                return lastKnownIndex = i;
            }
            System.out.println("------------->>>>>>>>>INDEX>>>>>" + i);
            System.out.println("------------->>>>>>>>MAX_WORDS_IN_REPOSITORY>>>>>>>>>>" + MAX_WORDS_IN_REPOSITORY);
        }
        throw new RuntimeException("too much objects in vocabulary to store it in!");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SomanticWord other = (SomanticWord) obj;
        if (!Objects.equals(this.lemma, other.lemma)) {
            return false;
        }
        return true;
    }

    synchronized public Set<SomanticAffect> getAffects() {
        return affects;
    }

    synchronized public void setAffects(Set<SomanticAffect> affects) {
        this.affects = affects;
    }

    synchronized public void addAffect(SomanticAffect affect) {
        this.affects.add(affect);
    }

    synchronized public void setDescription(String description) {
        this.description = description;
    }

    synchronized public String getDescription() {
        return description;
    }

    synchronized public Set<String> getWords() {
        return words;
    }

    synchronized public void setWords(Set<String> words) {
        this.words = words;
    }

    synchronized public void addWord(String word) {
        this.words.add(word);
    }

    synchronized public Set<SomanticWord> getPrevious() {
        return previous;
    }

    synchronized public void setPrevious(Set<SomanticWord> previous) {
        this.previous = previous;
    }

    synchronized public void addPrevious(SomanticWord previous) {
        this.previous.add(previous);
    }

    synchronized public Set<SomanticWord> getNext() {
        return next;
    }

    synchronized public void setNext(Set<SomanticWord> next) {
        this.next = next;
    }

    synchronized public void addNext(SomanticWord next) {
        this.next.add(next);
    }

    synchronized public Set<SomanticSentence> getSentences() {
        return this.sentences;
    }

    synchronized public void setSentences(Set<SomanticSentence> sentence) {
        this.sentences = sentence;
    }

    synchronized public void addSentence(SomanticSentence sentence) {
        if (sentence != null) {
            this.sentences.add(sentence);
        }
    }

    synchronized void addTag(String tag) {
        this.tags.add(tag);
    }

    synchronized Set<String> getTags() {
        return tags;
    }

    synchronized void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    public int compareTo(SomanticWord o) {
        if (lemma.hashCode() == o.lemma.hashCode()) {
            return 0;
        }
        return lemma.hashCode() > o.lemma.hashCode() ? 1 : -1;
    }
}

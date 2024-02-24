package sh.squeami.noble.settings.impl.interfaces;

public interface IEnumSetting {
    String getName();
    Enum<?> getEnum(String name);
}

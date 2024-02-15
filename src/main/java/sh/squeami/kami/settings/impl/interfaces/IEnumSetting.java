package sh.squeami.kami.settings.impl.interfaces;

public interface IEnumSetting {
    String getName();
    Enum<?> getEnum(String name);
}

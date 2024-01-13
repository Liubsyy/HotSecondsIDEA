package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public interface MyIcons {
    Icon HARRY_POTTER = IconLoader.getIcon("/icons/harry-potter.png", MyIcons.class);
    Icon MARIO = IconLoader.getIcon("/icons/super-mario.png", MyIcons.class);
    Icon MERMAID = IconLoader.getIcon("/icons/mermaid.png", MyIcons.class);
}

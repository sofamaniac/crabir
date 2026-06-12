# Compilation

To compile `rfm.flex`:

- using GrammarKit extension in the contextual menu of the file there is the `Run JFlex` option
- otherwise follow the instruction on JetBrains' fork of JFlex

Once compiled the code needs some editing:

- add the following imports

```java
import org.intellij.markdown.lexer.GeneratedLexer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
```

- change `.push` to `.add` and `.pop` to `.remove(stateStack.size() -1)`
- remove the constructor that requires an `in` parameter
- use the default override for `getState`
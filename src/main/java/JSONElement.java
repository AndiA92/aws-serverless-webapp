import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
class JSONElement {

    @JsonProperty("name")
    @Setter
    @Getter
    private String name;

    @JsonProperty("parent")
    @Setter
    @Getter
    private String parent;

    @JsonProperty("children")
    @Setter
    @Getter
    private List<JSONElement> children;

    @Override
    public String toString() {
        return "JSONElement{" +
                "name='" + name + '\'' +
                ", parent='" + parent + '\'' +
                ", children=" + children +
                '}';
    }
}

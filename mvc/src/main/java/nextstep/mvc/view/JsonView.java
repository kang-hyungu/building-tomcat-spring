package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        Optional<Object> renderObject = toJsonObject(model);
        render(renderObject, response.getOutputStream());
    }

    private void render(Optional<Object> renderObject, ServletOutputStream outputStream) throws IOException {
        if (renderObject.isPresent()) {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(outputStream, renderObject.get());
        }
    }

    private Optional<Object> toJsonObject(Map<String, ?> model) {
        if (model == null || model.isEmpty()) {
            return Optional.empty();
        }

        if (model.size() == 1) {
            return Optional.of(model.values().stream().findFirst().get());
        }

        return Optional.of(model);
    }
}

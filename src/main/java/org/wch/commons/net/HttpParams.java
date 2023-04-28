package org.wch.commons.net;

import lombok.*;
import lombok.experimental.Accessors;
import org.wch.commons.collections.CollectionUtils;

import java.io.*;
import java.util.*;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2023-04-27 11:23
 */
@Setter
@Getter
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class HttpParams implements Serializable {

    private String requestUrl;
    private Map<String, Object> pathParams;
    private Map<String, Object> queryParams;
    private Object bodyParams;
    private List<MultiPartFiles> files;
    private String certificateUrl;

    public Optional<String> getRequestURL() {
        return URLUtils.buildURL(this.requestUrl, pathParams, queryParams);
    }

    public Optional<String> getRequestURL(String paramPlaceholderRegex) {
        return URLUtils.buildURL(this.requestUrl, pathParams, queryParams, paramPlaceholderRegex);
    }

    @SneakyThrows
    public void addFile(File file) {
        if (CollectionUtils.isEmpty(files)) {
            this.files = new ArrayList<>();
        }
        this.files.add(new MultiPartFiles(file.getName(), new FileInputStream(file)));
    }

    @SneakyThrows
    public void addFile(String fileName, InputStream inputStream) {
        if (CollectionUtils.isEmpty(files)) {
            this.files = new ArrayList<>();
        }
        this.files.add(new MultiPartFiles(fileName, inputStream));
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class MultiPartFiles implements Serializable {

        private String fileName;

        private InputStream file;

        public void close() {
            if (Objects.isNull(file)) {
                return;
            }
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

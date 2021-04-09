package it.jump3.service;

import it.jump3.config.ConfigService;
import it.jump3.enumz.BusinessError;
import it.jump3.exception.CommonBusinessException;
import it.jump3.file.FileUploadForm;
import it.jump3.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
@Slf4j
public class FileService {

    @Inject
    ConfigService configService;


    public String getRootFolder() {
        return configService.getRootFolder();
    }

    public Integer getRetentionDays() {
        return configService.getRetentionDays();
    }

    public Path getBaseUrl() {
        return Paths.get(getRootFolder()).toAbsolutePath().normalize();
    }

    public Set<String> uploadOnFs(MultipartFormDataInput input) throws IOException {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");

        Set<String> listNames = new HashSet<>();
        for (InputPart inputPart : inputParts) {
            listNames.add(uploadOnFs(inputPart));
        }

        return listNames;
    }

    public String uploadOnFs(InputPart inputPart) throws IOException {

        MultivaluedMap<String, String> header = inputPart.getHeaders();
        String fileName = getFileName(header);

        //convert the uploaded file to inputstream
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        byte[] bytes = IOUtils.toByteArray(inputStream);

        // generate MD5 filename
        fileName = getFileName(inputStream, fileName);

        uploadOnFs(fileName, bytes);

        return fileName;
    }

    /**
     * header sample
     * {
     * Content-Type=[image/png],
     * Content-Disposition=[form-data; name="file"; filename="filename.extension"]
     * }
     **/
    private String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                return name[1].trim().replaceAll("\"", "");
            }
        }
        return "unknown";
    }

    /*public String uploadOnFs(FileUploadForm form) throws IOException {
        String filename;
        try (InputStream is = form.getData()) {
            filename = getFileName(is, form.getFileName());
            Path filePath = getFilePath(filename);
            FileUtil.uploadFile(filePath, is);
        }
        return filename;
    }*/

    public String uploadOnFs(FileUploadForm form) throws IOException {
        String filename;
        filename = getFileName(form.getData(), form.getFileName());
        Path filePath = getFilePath(filename);
        FileUtil.uploadFile(form.getData(), filePath.toString());
        return filename;
    }

    public String getFileName(InputStream is, String filename) throws IOException {
        return getFileName(is.readAllBytes(), filename);
    }

    public String getFileName(byte[] is, String filename) {
        return DigestUtils.md5Hex(is).concat(".").concat(FileUtil.getExt(filename).toLowerCase());
    }

    public void uploadOnFs(String fileName, byte[] file) throws IOException {
        Path filePath = getFilePath(fileName);
        try (InputStream is = new ByteArrayInputStream(file)) {
            FileUtil.uploadFile(filePath, is);
        }
    }

    public boolean deleteFileOnFs(String file, boolean filePath) {
        Path path = filePath ? Paths.get(file).toAbsolutePath().normalize() : getFilePath(file);
        boolean deleted = path.toFile().delete();
        if (!deleted) {
            log.warn("deleteFileOnFs -> I was unable to delete the file {}", path.toString());
        }
        return deleted;
    }

    public Path getFilePath(String fileName) {
        //String folderName = DateUtil.getAttachmentFolderName();
        //return getBaseUrl().resolve(folderName.concat("/").concat(fileName)).normalize();
        return getBaseUrl().resolve(fileName).normalize();
    }

    public byte[] loadFileAsBytes(String fileName) throws IOException, CommonBusinessException {
        loadFile(fileName);
        Path filePath = getFilePath(fileName);
        return Files.readAllBytes(filePath);
    }

    public byte[] loadFileAsBytesByPath(String filePath) throws IOException, CommonBusinessException {
        loadFileByPath(filePath);
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        return Files.readAllBytes(path);
    }

    public File loadFile(String fileName) throws CommonBusinessException {
        Path filePath = getFilePath(fileName);
        return loadFile(filePath);
    }

    public File loadFileByPath(String filePath) throws CommonBusinessException {
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        return loadFile(path);
    }

    private File loadFile(Path filePath) throws CommonBusinessException {
        File file = filePath.toFile();
        if (file.exists()) {
            return file;
        } else {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_404_FILE.code()),
                    String.format("File %s not found", file.getName()),
                    Response.Status.NOT_FOUND);

        }
    }
}

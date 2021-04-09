package it.jump3.file;

import it.jump3.enumz.FileType;
import it.jump3.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class FileUtil {

    public static final List<String> VALID_DOC_EXT = new LinkedList<>(Arrays.asList("png", "jpeg", "jpg", "pdf", "PNG", "JPEG", "JPG", "PDF"));
    public static final List<String> VALID_IMG_EXT = new LinkedList<>(Arrays.asList("png", "jpeg", "jpg", "PNG", "JPEG", "JPG"));
    public static final String PDF_EXT = "pdf";
    public static final String ZIP_EXT = "zip";
    private static final DateTimeFormatter dateFormatterFile = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");

    public static void uploadFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileOutputStream fop = new FileOutputStream(file)) {
            fop.write(content);
            fop.flush();
        }
    }

    public static void uploadFile(Path path, InputStream is) throws IOException {
        Files.deleteIfExists(path);
        Files.createDirectories(path.getParent());
        Files.createFile(path);
        Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
        //Files.write(path, IOUtils.toByteArray(is));
    }

    public static Response download(DownloadResponse downloadResponse) {

        Response.ResponseBuilder response = Response.ok(downloadResponse.getFile());
        response.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", downloadResponse.getFileName()));
        //response.header(HttpHeaders.CONTENT_TYPE, getExt(downloadResponse.getFileName()));
        response.header(HttpHeaders.CONTENT_TYPE, getContentType(downloadResponse.getFile()));
        response.header(HttpHeaders.CONTENT_LENGTH, downloadResponse.getFile().length());

        return response.build();
    }

    public static String getContentType(File file) {

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            log.warn("Error during conversion contentType");
        }

        // Fallback to the default content type if type could not be determined
        if (StringUtils.isEmpty(contentType)) {
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return contentType;
    }

    public static List<File> getFiles(File folder, String nameFind) {
        File[] files = folder.listFiles((dir, name) -> name.startsWith(nameFind) &&
                VALID_DOC_EXT.contains(getExt(name).toLowerCase()));
        return files != null ? new ArrayList<>(Arrays.asList(files)) : new ArrayList<>();
    }

    public static List<File> getFiles(Path baseUrl, List<AttachmentFile> attachmentFiles) {

        List<File> files = new ArrayList<>();
        attachmentFiles.forEach(attachmentFileDTO -> {

            try {
                String tempFileName = baseUrl.resolve(attachmentFileDTO.getFileName()).toString();
                File tempFile = new File(tempFileName);
                FileUtils.writeByteArrayToFile(tempFile, attachmentFileDTO.getSource());
                files.add(tempFile);
            } catch (IOException e) {
                log.error("Error during write temp file {} on FS: {}", attachmentFileDTO.getFileName(), e.getMessage());
            }
        });

        return files;
    }

    public static String getExt(String name) {
        return FilenameUtils.getExtension(name);
        //return URLConnection.guessContentTypeFromName(name);
    }

    public static String getFileNameWithoutExt(String name) {
        return normalizeFileName(FilenameUtils.removeExtension(name));
    }

    public static String normalizeFileName(String fileName) {
        return fileName.replaceAll("\\W+", "_");
    }

//    public static void main(String[] args) throws IOException, DocumentException {
//        Path baseUrl = Paths.get("/mysisal".concat("/").concat("insurance")).toAbsolutePath().normalize();
//        File img = new File("/mysisal/insurance/1024.jpg");
//        convertImgToPdf(img, baseUrl);
//    }

    public static List<String> getFileNameSet(List<File> files) {
        return files.stream().map(File::getName).collect(Collectors.toList());
    }

    public static byte[] getBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public static byte[] getBytes(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    /**
     * Delete list of files
     *
     * @param files
     */
    public static void deleteFiles(List<File> files) {
        files.forEach(FileUtil::deleteFile);
    }

    public static void deleteFile(File file) {
        if (file.isFile()) {
            if (file.exists()) {
                if (!file.delete()) {
                    log.warn("File {} not deleted", file.getName());
                }
            }
        } else if (file.isDirectory()) {
            if (file.listFiles() != null && Objects.requireNonNull(file.listFiles()).length > 0) {
                deleteFiles(Arrays.stream(Objects.requireNonNull(file.listFiles())).collect(Collectors.toList()));
            }
        }
    }

    /**
     * Zip list of files
     *
     * @param files
     * @param zipFileName
     * @throws IOException
     */
    public static void zipFiles(List<File> files, String zipFileName) throws IOException {

        String ext = getExt(zipFileName);
        if (StringUtils.isEmpty(ext)) {
            zipFileName += "." .concat(ZIP_EXT);
        } else if (!ext.equalsIgnoreCase(ZIP_EXT)) {
            zipFileName = getFileNameWithoutExt(zipFileName).concat(".").concat(ZIP_EXT);
        }

        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            for (File fileToZip : files) {
                try (FileInputStream fis = new FileInputStream(fileToZip)) {

                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }

                    zipOut.closeEntry();
                }
            }
        }
    }

    /**
     * Zip file or directory
     *
     * @param directory
     * @param zipFileName
     * @throws IOException
     */
    public static void zipDirectory(File directory, String zipFileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            zipFile(directory, directory.getName(), zipOut);
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName.concat("/")));
            }
            zipOut.closeEntry();
            if (fileToZip.listFiles() != null && Objects.requireNonNull(fileToZip.listFiles()).length > 0) {
                for (File childFile : Objects.requireNonNull(fileToZip.listFiles())) {
                    zipFile(childFile, fileName.concat("/").concat(childFile.getName()), zipOut);
                }
            }
        } else if (fileToZip.isFile()) {
            try (FileInputStream fis = new FileInputStream(fileToZip)) {

                ZipEntry zipEntry = new ZipEntry(fileName);
                zipOut.putNextEntry(zipEntry);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
            }
        }
    }

    public static boolean searchByFileType(FileType fileType, Path path) {
        switch (fileType) {
            case ALL:
                return Files.isRegularFile(path) || Files.isDirectory(path);
            case FILE:
                return Files.isRegularFile(path);
            case FOLDER:
                return Files.isDirectory(path);
            default:
                throw new IllegalArgumentException("Unexpected FileType " .concat(fileType.name()));
        }
    }

    public static String getCsvFileName(String suffix) {
        return DateUtil.nowLocalDateTimeItaly().format(dateFormatterFile).concat("_").concat(suffix).concat(".csv");
    }

    public static String getFileName(Long id, String fileName) {
        return id + "_" + getFileName(fileName);
    }

    public static String getFileName(String fileName) {
        return Paths.get(getFileNameWithoutExt(fileName).concat(".").concat(FileUtil.getExt(fileName))).toAbsolutePath().normalize().toString();
    }

    public static void retentionFiles(Path basePath, String rootFolder, Integer retentionDays) {

        final LocalDateTime dateToCheck = DateUtil.nowLocalDate(DateUtil.ZONE_ID_ITALY).atStartOfDay().minusDays(retentionDays);
        List<Path> pathList = getAllFiles(basePath, rootFolder);
        pathList
                .forEach(path -> {
                    try {
                        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                        FileTime fileTime = attr.creationTime();
                        LocalDateTime creationTime = LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
                        if (creationTime.isBefore(dateToCheck)) {
                            if (!path.toFile().delete()) {
                                log.warn("retentionFiles -> I was unable to delete the file {}", path.toFile().getName());
                            }
                        }
                    } catch (IOException e) {
                        log.error("Error during retrieve creationTime of file {}", path.toFile().getName());
                    }
                });
    }

    private static List<Path> getAllFiles(Path basePath, String rootFolder) {

        List<Path> outcome = new ArrayList<>();
        List<Path> fileAndDirs = new ArrayList<>();

        //Path basePath = getBaseUrl();
        try (Stream<Path> walk = Files.walk(basePath)) {
            fileAndDirs = walk.filter(path -> !path.equals(basePath) && FileUtil.searchByFileType(FileType.ALL, path))
                    //.map(x -> x.toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Error during retrieve files of folder {}", rootFolder);
        }

        fileAndDirs.forEach(path -> {
            if (path.toFile().isFile()) {
                outcome.add(path);
            } else if (path.toFile().isDirectory()) {
                addAllFilesFromDirectory(path, outcome);
            }
        });

        return outcome;
    }

    private static void addAllFilesFromDirectory(Path directory, List<Path> files) {

        //Assert.isTrue(directory.toFile().isDirectory(), "Path must be a directory");

        // Get all files from a directory.
        File[] listFiles = directory.toFile().listFiles();
        if (listFiles != null && listFiles.length > 1)
            for (File file : listFiles) {
                if (file.isFile()) {
                    files.add(file.toPath());
                } else if (file.isDirectory()) {
                    addAllFilesFromDirectory(file.toPath(), files);
                }
            }
    }
}

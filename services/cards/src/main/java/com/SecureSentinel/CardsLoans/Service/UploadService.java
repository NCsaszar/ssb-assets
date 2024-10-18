package com.SecureSentinel.CardsLoans.Service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;

@Service
public class UploadService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job cardOfferJob;

    @Autowired
    private Job loanJob;

    private Path lastProcessedFile;

    public boolean validateFileFormat(MultipartFile file) {
        return "text/csv".equals(file.getContentType());
    }

    public boolean validateFileSize(MultipartFile file) {
        final long MAX_SIZE = 50 * 1024 * 1024;
        return file.getSize() <= MAX_SIZE;
    }

    public void setLastProcessedFile(Path filePath) {
        this.lastProcessedFile = filePath;
    }


    public String startRunJob(MultipartFile file) {
        String jobId = null;

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .addDate("run.date", new Date())
                    .toJobParameters();

            jobLauncher.run(cardOfferJob, jobParameters);

            jobId = jobParameters.getString("JobID");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jobId;
    }

    public String startLoanJob(MultipartFile file){
        String jobId = null;

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .addDate("run.date", new Date())
                    .toJobParameters();

            jobLauncher.run(loanJob, jobParameters);

            jobId = jobParameters.getString("JobID");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jobId;
    }



    public void retriggerJob() throws Exception {
        if (lastProcessedFile != null) {
            JobParameters params = new JobParametersBuilder()
                    .addString("input.file", lastProcessedFile.toString())
                    .addLong("timestamp", System.currentTimeMillis(), true)
                    .toJobParameters();
            jobLauncher.run(cardOfferJob, params);
        } else {
            throw new IllegalStateException("No file path available for retriggering the job.");
        }
    }
}


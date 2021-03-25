package com.itest4u.itel.implementations;

import com.itest4u.itel.interfaces.IAttemptCallback;
import com.itest4u.itel.interfaces.IExecutable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.itest4u.itel.interfaces.IRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Execute implements IExecutable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Execute.class);
    private String name;

    public Execute(String name) {
        this.name = name;
    }

    public void finalize() {
    }
    class RestoreItem {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public IRunner getRunnable() {
            return runnable;
        }

        public void setRunnable(IRunner runnable) {
            this.runnable = runnable;
        }

        private IRunner runnable;
    }

    @Override
    public IExecutable run(IRunner r) {
        Allure.step(name, r::run);
        return this;
    }

    static Map<String,Stack<RestoreItem>> restoreStack = new HashMap<>();

    @Override
    public IExecutable parallel(IRunner... allRunner) {
        AtomicInteger i = new AtomicInteger(0);
        AllureLifecycle alc = Allure.getLifecycle();
        String parentID = UUID.randomUUID().toString();
        StepResult stepResult = new StepResult();
        stepResult.setName(name + " [Parallel] ");
        stepResult.setStatus(Status.PASSED);
        Allure.getLifecycle().startStep(parentID, stepResult);
            Allure.getLifecycle().updateStep(parentID,r -> r.setStatus(Status.PASSED));
            Arrays.stream(allRunner).parallel().forEach((runner) -> {
                String stepID = UUID.randomUUID().toString();
                StepResult result = new StepResult();
                result.setName(name + "-parallel-" + String.valueOf(i.incrementAndGet()));
                result.setStatus(Status.PASSED);
                Allure.getLifecycle().startStep(parentID, stepID, result);
                try {
                    runner.run();
                    Allure.getLifecycle().updateStep(r -> r.setStatus(Status.PASSED));
                } catch (Exception e) {
                    Allure.getLifecycle().updateStep(r -> r.setStatus(Status.FAILED));
                    Allure.getLifecycle().updateStep(parentID,r -> r.setStatus(Status.FAILED));
                    Allure.getLifecycle().stopStep(stepID);
                    Allure.getLifecycle().stopStep(parentID);
                    throw e;
                }
                Allure.getLifecycle().stopStep(stepID);
            });

        Allure.getLifecycle().stopStep(parentID);
        return this;
    }

    public IExecutable restore(IRunner r) {
        String taskcaseExecID = Allure.getLifecycle().getCurrentTestCase().get();
        Stack<RestoreItem> stack = restoreStack.computeIfAbsent(taskcaseExecID, k -> new Stack<>());
        RestoreItem item = new RestoreItem();
        item.setName(name);
        item.setRunnable(r);
        stack.push(item);
        return this;
    }



    @Override
    public IExecutable attempt(IRunner r, int maxRetries) throws Exception {
        boolean isSuccessful = false;
        for (int i=0;i<maxRetries;i++) {
            try {
                Allure.step(name+"-attempt-"+String.valueOf(i+1), r::run);
                isSuccessful = true;
                break;
            } catch (Exception e) {

            }
        }
        if (!isSuccessful) {
            throw new Exception("Attempt sdsfsdfsd");
        }
        return this;
    }

    public IExecutable attempt(final IAttemptCallback c, int maxRetries) throws Exception {

        Allure.step(name + "[Attempt " + maxRetries +  " ]", () -> {
            boolean isSuccessful = false;
            for (int i=0;i<maxRetries;i++) {
                try {
                    final int j=i;
                    Allure.step(name+"-attempt-"+String.valueOf(i+1), () -> {
                        c.call(j);
                    });
                    isSuccessful = true;
                    break;
                } catch (Exception e) {

                }
            }
            if (!isSuccessful) {
                throw new Exception("All " + maxRetries + "attempt failed");
            }
        });


        return this;
    }

    public static boolean isRestoreStackEmpty() {
        String taskcaseExecID = Allure.getLifecycle().getCurrentTestCase().orElse("");
        return restoreStack.get(taskcaseExecID) == null;

    }


    public static void popRestore() {
        boolean hasExceptions = false;
        String taskcaseExecID = Allure.getLifecycle().getCurrentTestCase().orElse("");
        Stack<RestoreItem> stack = restoreStack.get(taskcaseExecID);



        while (stack != null && ! stack.isEmpty()) {
            RestoreItem item =stack.pop();
            try {
                Allure.step("Restoration (" + item.getName() + ")..." ,item.getRunnable()::run);
            } catch (Exception e) {
                hasExceptions = true;

            }


        }
        if (hasExceptions) {
            throw new RuntimeException();
        }

    }
}


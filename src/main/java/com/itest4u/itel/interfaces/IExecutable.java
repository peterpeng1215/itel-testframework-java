package com.itest4u.itel.interfaces;

public interface IExecutable {
    public IExecutable run(IRunner r);
    public IExecutable parallel(IRunner... r);
    public IExecutable restore(IRunner r);
    public IExecutable attempt(IRunner r, int maxRetries) throws Exception;
    public IExecutable attempt(IAttemptCallback r, int maxRetries) throws Exception;
}

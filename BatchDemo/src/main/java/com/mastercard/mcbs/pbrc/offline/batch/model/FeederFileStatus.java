package com.mastercard.mcbs.pbrc.offline.batch.model;

public enum FeederFileStatus {
    READY,
    PROCESSING,
    COMPLETED,
    UNKNOWN,
    NOOP,
    FAILED,
    STOPPED,
    EXECUTING;
}

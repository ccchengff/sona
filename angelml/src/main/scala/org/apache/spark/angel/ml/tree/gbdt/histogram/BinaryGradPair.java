package org.apache.spark.angel.ml.tree.gbdt.histogram;

import org.apache.spark.angel.ml.tree.gbdt.tree.GBDTParam;

import java.io.Serializable;

public class BinaryGradPair implements GradPair, Serializable {
    private double grad;
    private double hess;

    public BinaryGradPair() {}

    public BinaryGradPair(double grad, double hess) {
        this.grad = grad;
        this.hess = hess;
    }

    @Override
    public void plusBy(GradPair gradPair) {
        this.grad += ((BinaryGradPair) gradPair).grad;
        this.hess += ((BinaryGradPair) gradPair).hess;
    }

    public void plusBy(double grad, double hess) {
        this.grad += grad;
        this.hess += hess;
    }

    @Override
    public void subtractBy(GradPair gradPair) {
        this.grad -= ((BinaryGradPair) gradPair).grad;
        this.hess -= ((BinaryGradPair) gradPair).hess;
    }

    public void subtractBy(double grad, double hess) {
        this.grad -= grad;
        this.hess -= hess;
    }

    @Override
    public GradPair plus(GradPair gradPair) {
        GradPair res = this.copy();
        res.plusBy(gradPair);
        return res;
    }

    public GradPair plus(double grad, double hess) {
        return new BinaryGradPair(this.grad + grad, this.hess + hess);
    }

    @Override
    public GradPair subtract(GradPair gradPair) {
        GradPair res = this.copy();
        res.subtractBy(gradPair);
        return res;
    }

    public GradPair subtract(double grad, double hess) {
        return new BinaryGradPair(this.grad - grad, this.hess - hess);
    }

    @Override
    public void timesBy(double x) {
        this.grad *= x;
        this.hess *= x;
    }

    @Override
    public boolean satisfyWeight(GBDTParam param) {
        return param.satisfyWeight(hess);
    }

    @Override
    public float calcGain(GBDTParam param) {
        return (float) param.calcGain(grad, hess);
    }

    @Override
    public float calcWeight(GBDTParam param) {
        return (float) param.calcWeight(grad, hess);
    }

    @Override
    public float[] calcWeights(GBDTParam param) {
        throw new RuntimeException(String.format("%s does not support multi-class task",
                this.getClass().getSimpleName()));
    }

    @Override
    public BinaryGradPair copy() {
        return new BinaryGradPair(grad, hess);
    }

    @Override
    public void clear() {
        this.grad = 0.0;
        this.hess = 0.0;
    }

    public double getGrad() {
        return grad;
    }

    public double getHess() {
        return hess;
    }

    public void setGrad(double grad) {
        this.grad = grad;
    }

    public void setHess(double hess) {
        this.hess = hess;
    }

    public void set(double grad, double hess) {
        this.grad = grad;
        this.hess = hess;
    }

    @Override
    public String toString() {
        return "(" + grad + ", " + hess + ")";
    }
}

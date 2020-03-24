/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_customer_client.visitor;

/**
 *
 * @author f023507i
 */
public interface Visitable
{
    void accept(Visitor visitor);
}

class CustomersController < ApplicationController
  before_filter :authorize_secretary!, :only => [:new, :create, :edit, :update, :destroy]

  # GET /customers
  def index
    @customers = Customer.all
  end

  # GET /customers/1
  def show
    @customer = Customer.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
    end
  end

  # GET /customers/new
  def new
    @customer = Customer.new

    respond_to do |format|
      format.html # new.html.erb
    end
  end

  # GET /customers/1/edit
  def edit
    @customer = Customer.find(params[:id])
  end

  # POST /customers
  def create
    @customer = Customer.new(params[:customer])

    if @customer.save
      redirect_to(@customer, :notice => Customer.model_name.human + ' ' + t(:create_successful))
    else
      render :action => "new"
    end
  end

  # PUT /customers/1=
  def update
    @customer = Customer.find(params[:id])

    if @customer.update_attributes(params[:customer])
      redirect_to(@customer, :notice => Customer.model_name.human + ' ' + t(:update_successful))
    else
      render :action => "edit"
    end
  end

  # DELETE /customers/1
  def destroy
    @customer = Customer.find(params[:id])
    @customer.destroy

    redirect_to(customers_url)
  end

  def synchronize
    if params[:last_update].nil?
      @updated_customers = Customer.all
    else
      last_update = Time.parse(params[:last_update])
      @updated_customers = Customer.updated_after(last_update)
    end
    render :json => @updated_customers.to_json(:include => { :address => {:include => :gps_position } } )
  end
end

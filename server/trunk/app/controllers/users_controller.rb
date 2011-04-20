class UsersController < ApplicationController
  before_filter :authorize_user!, :only => [:new, :create, :edit, :update, :destroy]

  # GET /users
  def index
    @users = User.all
    @user_types = User::TYPES

    respond_to do |format|
      format.html # index.html.erb
    end
  end

  # GET /users/1
  def show
    @user = User.find(params[:id])
    @user_types = User::TYPES
  end

  # GET /users/new
  def new
    @user = User.new
    @user_types = User::TYPES
  end

  # GET /users/1/edit
  def edit
    @user = User.find(params[:id])
    @user_types = User::TYPES
  end

  # POST /users
  def create
    @user = User.new(params[:user])
    @user_types = User::TYPES

    if @user.save
      redirect_to(@user, :notice => 'User was successfully created.')
    else
      render :action => "new"
    end
  end

  # PUT /users/1
  def update
    @user = User.find(params[:id])

    if @user.update_attributes(params[:user])
      redirect_to(@user, :notice => 'User was successfully updated.')
    else
      render :action => "edit"
    end
  end

  # DELETE /users/1
  def destroy
    @user = User.find(params[:id])
    @user.destroy

    redirect_to(users_url)
  end
end

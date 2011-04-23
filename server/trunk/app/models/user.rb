class User < ActiveRecord::Base
  # TODO: Remove admin type. Secretary type is admin
  # TODO: Rename employee --> a secretary is a employee...
  TYPES = { :ADMIN => 1, :SECRETARY => 2, :FIELD_WORKER => 3 }

  # Include default devise modules. Others available are:
  # :token_authenticatable, :encryptable, :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :rememberable
  
  # Setup accessible (or protected) attributes for your model
  attr_accessible :email, :password, :remember_me, :first_name, :last_name, :user_type, :password_salt
  validates :password, :presence => true, :on => :create

  def admin?; user_type == TYPES[:ADMIN]; end
  def secretary?; user_type == TYPES[:SECRETARY]; end
  def field_worker?; user_type == TYPES[:FIELD_WORKER]; end
end
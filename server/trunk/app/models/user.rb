class User < ActiveRecord::Base
  has_many :time_entries
  
  TYPE_SECRETARY = 1
  TYPE_FIELD_WORKER = 2
  TYPES = { :SECRETARY => TYPE_SECRETARY, :FIELD_WORKER => TYPE_FIELD_WORKER }

  # Include default devise modules. Others available are:
  # :token_authenticatable, :encryptable, :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :rememberable
  
  # Setup accessible (or protected) attributes for your model
  # attr_accessible :email, :password, :remember_me, :first_name, :last_name, :user_type, :password_salt, :updated_at

  validates :password, :presence => true, :on => :create
  validates :email, :presence => true

  def secretary?; user_type == TYPE_SECRETARY; end
  def field_worker?; user_type == TYPE_FIELD_WORKER; end
end
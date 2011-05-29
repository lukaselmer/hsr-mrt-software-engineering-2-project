# Holds information about a User (see Devise)
class User < ActiveRecord::Base
  has_many :time_entries
  
  TYPE_SECRETARY = "Secretary"
  TYPE_FIELD_WORKER = "FieldWorker"
  TYPES = { :secretary => TYPE_SECRETARY, :field_worker => TYPE_FIELD_WORKER }

  # Include default devise modules. Others available are:
  # :token_authenticatable, :encryptable, :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :rememberable
  
  # Setup accessible (or protected) attributes for your model
  # attr_accessible :email, :password, :remember_me, :first_name, :last_name, :user_type, :password_salt, :updated_at

  validates :password, :presence => true, :on => :create
  validates :email, :presence => true

  scope :field_workers, where(:type => TYPE_FIELD_WORKER)
  scope :secretaries, where(:type => TYPE_SECRETARY)

  def secretary?; is_a? Secretary; end
  def field_worker?; is_a? FieldWorker; end
  def human_model_name; self.class.model_name.human; end

  def self.type_for_select
    [[Secretary.model_name.human, TYPE_SECRETARY], [FieldWorker.model_name.human, TYPE_FIELD_WORKER]]
  end

  def self.for_select
    find(:all, :order => :first_name).collect { |u| [u, u.id] }
  end

  def to_s
    [first_name, last_name].join(' ')
  end
end

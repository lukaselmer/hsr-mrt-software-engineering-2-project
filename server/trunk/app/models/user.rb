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

  def secretary?; type == TYPE_SECRETARY; end
  def field_worker?; type == TYPE_FIELD_WORKER; end

  def type_human_name
    return secretary_human_name if secretary?
    return field_worker_human_name if field_worker?
    raise 'Invalid user type'
  end

  def self.type_for_select
    [[secretary_human_name, TYPE_SECRETARY], [field_worker_human_name, TYPE_FIELD_WORKER]]
  end

  def self.secretary_human_name; human_attribute_name(:user_type_secretary); end
  def self.field_worker_human_name; human_attribute_name(:user_type_field_worker); end

  def self.for_select(type=nil)
    set = all
    set = find_all_by_type type if TYPES.has_value?(type)
    set.to_a.collect { |u| [u, u.id] }
  end

  def to_s
    [first_name, last_name].join(' ')
  end
end